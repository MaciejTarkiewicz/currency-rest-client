package pl.tarkiewicz.currencyrestclient;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.tarkiewicz.currencyrestclient.getDto.GetResponseDto;
import pl.tarkiewicz.currencyrestclient.getDto.RateDto;
import pl.tarkiewicz.currencyrestclient.postDto.Command;
import pl.tarkiewicz.currencyrestclient.postDto.CurrencyDto;
import pl.tarkiewicz.currencyrestclient.postDto.PostResponseDto;
import pl.tarkiewicz.currencyrestclient.service.CurrencyGetService;
import pl.tarkiewicz.currencyrestclient.service.CurrencyPostService;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class CurrencyControllerTest {

    CurrencyGetService currencyGetService = Mockito.mock(CurrencyGetService.class);
    CurrencyPostService currencyPostService = new CurrencyPostService();
    CurrencyController currencyController = new CurrencyController(currencyGetService, currencyPostService);

    @Test
    public void shouldGetCurrencyExchange() throws JsonProcessingException {
        mockDataFromApi();
        assertEquals(expectedResultFromGet(), getCurrencyExchange().getBody());
        assertEquals(HttpStatus.OK, getCurrencyExchange().getStatusCode());

    }

    @Test
    public void shouldNotGetCurrencyExchange() {
        given(currencyGetService.getDataFromApi("BTC", List.of("BNB"))).willReturn(Optional.empty());
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, getCurrencyExchange().getStatusCode());
    }

    @Test
    public void shouldExchangeCurrency() throws JsonProcessingException {
        mockDataFromApi();
        assertEquals(expectedResultFromPost(), calculateCurrencyExchange().getBody());

    }

    @Test
    public void shouldNotExchangeCurrency() {
        given(currencyGetService.getDataFromApi("BTC", List.of("BNB"))).willReturn(Optional.empty());
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, calculateCurrencyExchange().getStatusCode());

    }

    private void mockDataFromApi() throws JsonProcessingException {
        given(currencyGetService.getDataFromApi("BTC", List.of("BNB"))).willReturn(Optional.of("{\n" +
                "  \"asset_id_base\": \"BTC\",\n" +
                "  \"rates\": [\n" +
                "    {\n" +
                "      \"time\": \"2020-04-18T18:01:00.9396734Z\",\n" +
                "      \"asset_id_quote\": \"BNB\",\n" +
                "      \"rate\": 3\n" +
                "    }\n" +
                "  ]\n" +
                "}"));
        when(currencyGetService.mapDataToResponse(Mockito.any())).thenCallRealMethod();
    }

    private ResponseEntity<?> getCurrencyExchange() {
        return currencyController.getCurrencyExchange("BTC", List.of("BNB"));
    }

    private ResponseEntity<?> calculateCurrencyExchange() {
        return currencyController.exchangeCurrency(prepareCommand());
    }

    private GetResponseDto expectedResultFromGet() {
        RateDto rateDto = new RateDto("BNB", new BigDecimal("3"));
        GetResponseDto getResponseDto = new GetResponseDto();
        getResponseDto.setRates(List.of(rateDto));
        getResponseDto.setSource("BTC");
        return getResponseDto;
    }

    private PostResponseDto expectedResultFromPost() {
        return PostResponseDto.builder()
                .from("BTC")
                .to(Map.of("BNB", currencyDtoCreator(100, new BigDecimal("3.00"),
                        new BigDecimal("303.00"), new BigDecimal("3"))))
                .build();
    }

    private Command prepareCommand() {
        Command command = new Command();
        command.setFrom("BTC");
        command.setAmount(100);
        command.setTo(List.of("BNB"));
        return command;
    }

    private CurrencyDto currencyDtoCreator(Integer amount, BigDecimal fee, BigDecimal result, BigDecimal rate) {
        return CurrencyDto.builder()
                .amount(amount)
                .fee(fee)
                .result(result)
                .rate(rate)
                .build();
    }

}