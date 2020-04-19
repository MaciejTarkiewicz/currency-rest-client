package pl.tarkiewicz.currencyrestclient;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.tarkiewicz.currencyrestclient.getDto.GetResponseDto;
import pl.tarkiewicz.currencyrestclient.postDto.Command;
import pl.tarkiewicz.currencyrestclient.result.GetOperationResult;
import pl.tarkiewicz.currencyrestclient.result.PostOperationResult;
import pl.tarkiewicz.currencyrestclient.service.CurrencyGetService;
import pl.tarkiewicz.currencyrestclient.service.CurrencyPostService;

@RestController
@AllArgsConstructor
public class CurrencyController {

    private final CurrencyGetService currencyGetService;
    private final CurrencyPostService currencyPostService;

    @GetMapping("/currencies/{currency}")
    public ResponseEntity<?> getCurrency(@PathVariable String currency, @RequestParam(required = false) List<String> filter) {
        ObjectMapper objectMapper = new ObjectMapper();
        Optional<String> body = currencyGetService.prepareBody(currency, filter);
        return body.map(request -> {
            try {
                return GetOperationResult.success(objectMapper.readValue(request, GetResponseDto.class));
            } catch (JsonProcessingException e) {
                return GetOperationResult.failure(e.getMessage());
            }
        }).orElse(GetOperationResult.failure("Bad Request"));

    }

    @PostMapping("/currencies/exchange")
    public ResponseEntity<?> CalculateCurrency(@RequestBody Command command) {
        GetResponseDto getResponseDto = getGetResponseDto(command);
        Optional<GetResponseDto> cos = Optional.ofNullable(getResponseDto);
        return cos.map(responseDto -> {
            try {
                return currencyPostService.successResultCreator(command, responseDto);
            } catch (Exception e) {
                return PostOperationResult.failure("Bad Request!");
            }
        }).orElse(PostOperationResult.failure("Bad Request!"));

    }

    private GetResponseDto getGetResponseDto(Command command) {
        ResponseEntity<?> getResponseDtoEntity = getCurrency(command.getFrom(), command.getTo());
        return (GetResponseDto) getResponseDtoEntity.getBody();
    }

}
