package pl.tarkiewicz.currencyrestclient;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.tarkiewicz.currencyrestclient.getDto.GetResponseDto;
import pl.tarkiewicz.currencyrestclient.postDto.Command;
import pl.tarkiewicz.currencyrestclient.result.OperationResult;
import pl.tarkiewicz.currencyrestclient.service.CurrencyGetService;
import pl.tarkiewicz.currencyrestclient.service.CurrencyPostService;

@RestController
@AllArgsConstructor
public class CurrencyController {

    private final CurrencyGetService currencyGetService;
    private final CurrencyPostService currencyPostService;

    @GetMapping("/currencies/{currency}")
    public ResponseEntity<?> getCurrencyExchange(@PathVariable String currency, @RequestParam(required = false) List<String> filter) {
        Optional<String> body = currencyGetService.getDataFromApi(currency, filter);
        return body.map(request -> {
            try {
                return OperationResult.success(currencyGetService.mapDataToResponse(request));
            } catch (JsonProcessingException e) {
                return OperationResult.failureRequest(e.getMessage());
            }
        }).orElse(OperationResult.failureExternalApi());

    }

    @PostMapping("/currencies/exchange")
    @SuppressWarnings("ConstantConditions")
    public ResponseEntity<?> exchangeCurrency(@RequestBody Command command) {
        ResponseEntity<?> responseEntity = getResponse(command);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return OperationResult.success(currencyPostService.successResultCreator(command, (GetResponseDto) responseEntity.getBody()));
        } else {
            return responseEntity;
        }
    }

    private ResponseEntity<?> getResponse(Command command) {
        return getCurrencyExchange(command.getFrom(), command.getTo());
    }

}
