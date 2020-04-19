package pl.tarkiewicz.currencyrestclient;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CurrencyController {

    private static final String URL = "https://rest.coinapi.io/v1/exchangerate/";

    @Value("${api.key}")
    private String key;

    @GetMapping("/currencies/{currency}")
    public Dto getAll(@PathVariable String currency, @RequestParam(required = false) List<String> filter) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-CoinAPI-Key", key);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        //tutaj obluzyc zeby nie bylo 500
        String body = restTemplate.exchange(createUrl(currency, filter), HttpMethod.GET, entity, String.class).getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(body, Dto.class);
    }

    private String createUrl(String currency, List<String> filter) {
        if (filter == null || filter.isEmpty()) {
            return URL + currency;
        } else {
            return URL + currency + "?filter_asset_id=" + String.join(",", filter);
        }

    }

    @PostMapping("/currencies/exchange")
    public ResponseDto add(@RequestBody PostDto postDto) throws JsonProcessingException {
        Dto dto = getAll(postDto.getFrom(), postDto.getTo());
        return ResponseDto.builder()
                .from(dto.source)
                .currencyDtos(dto.getRates().parallelStream()
                        .collect(Collectors.toMap(Rate::getName
                                , r -> {
                                    BigDecimal zmienna = r.getRate().multiply(new BigDecimal("0.01")).multiply(new BigDecimal(postDto.getAmount()));
                                    return CurrencyDto.builder()
                                            .amount(postDto.getAmount())
                                            .fee(zmienna)
                                            .rate(r.getRate())
                                            .result(new BigDecimal(postDto.getAmount()).multiply(r.getRate()).add(zmienna))
                                            .build();
                                })))

                .build();

    }

}
