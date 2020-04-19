package pl.tarkiewicz.currencyrestclient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import pl.tarkiewicz.currencyrestclient.getDto.GetResponseDto;
import pl.tarkiewicz.currencyrestclient.getDto.RateDto;
import pl.tarkiewicz.currencyrestclient.postDto.Command;
import pl.tarkiewicz.currencyrestclient.postDto.CurrencyDto;
import pl.tarkiewicz.currencyrestclient.postDto.PostResponseDto;
import pl.tarkiewicz.currencyrestclient.result.OperationResult;

@RestController
public class CurrencyController {

    private static final String URL = "https://rest.coinapi.io/v1/exchangerate/";

    private static final BigDecimal provision = new BigDecimal("0.01");

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/currencies/{currency}")
    public ResponseEntity<?> getCurrency(@PathVariable String currency, @RequestParam(required = false) List<String> filter) {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        Optional<String> body = currencyService.prepareBody(currency, filter, restTemplate);
        return body.map(request -> {
            try {
                return OperationResult.success(objectMapper.readValue(request, GetResponseDto.class));
            } catch (JsonProcessingException e) {
                return OperationResult.failure(e.getMessage());
            }
        }).orElse(OperationResult.failure("Bad Request"));

    }

    @PostMapping("/currencies/exchange")
    public PostResponseDto add(@RequestBody Command command) throws JsonProcessingException {
        ResponseEntity<?> getResponseDtoEntity = getCurrency(command.getFrom(), command.getTo());
        GetResponseDto getResponseDto = (GetResponseDto) getResponseDtoEntity.getBody();
        return PostResponseDto.builder()
                .from(getResponseDto.getSource())
                .to(getResponseDto.getRates().parallelStream()
                        .collect(Collectors.toMap(RateDto::getName
                                , r -> {
                                    BigDecimal fee = r.getRate().multiply(provision).multiply(new BigDecimal(command.getAmount()));
                                    return CurrencyDto.builder()
                                            .amount(command.getAmount())
                                            .fee(fee)
                                            .rate(r.getRate())
                                            .result(new BigDecimal(command.getAmount()).multiply(r.getRate()).add(fee))
                                            .build();
                                })))

                .build();

    }

}
