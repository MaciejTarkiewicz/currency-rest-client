package pl.tarkiewicz.currencyrestclient;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.tarkiewicz.currencyrestclient.getDto.GetResponseDto;
import pl.tarkiewicz.currencyrestclient.getDto.RateDto;
import pl.tarkiewicz.currencyrestclient.service.CurrencyGetService;
import pl.tarkiewicz.currencyrestclient.service.CurrencyPostService;

@RunWith(MockitoJUnitRunner.class)
public class CurrencyControllerTest {

    @Spy
    CurrencyGetService currencyGetService = Mockito.mock(CurrencyGetService.class);
    @Spy
    CurrencyPostService currencyPostService = Mockito.mock(CurrencyPostService.class);

    CurrencyController currencyController = new CurrencyController(currencyGetService, currencyPostService);

    @Test
    public void shouldGetCurrencyExchange() throws JsonProcessingException {
        mockDataFromApi();
        assertEquals(expectedResult(), getCurrencyExchange().getBody());
        assertEquals(HttpStatus.OK, getCurrencyExchange().getStatusCode());

    }

    @Test
    public void shouldNotGetCurrencyExchange() {
        given(currencyGetService.getDataFromApi("BTC", List.of("BNB"))).willReturn(Optional.empty());
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, getCurrencyExchange().getStatusCode());
    }

    @Test
    public void shouldExchangeCurrency() {
    }

    private void mockDataFromApi() throws JsonProcessingException {
        given(currencyGetService.getDataFromApi("BTC", List.of("BNB"))).willReturn(Optional.of("{\n" +
                "  \"asset_id_base\": \"BTC\",\n" +
                "  \"rates\": [\n" +
                "    {\n" +
                "      \"time\": \"2020-04-18T18:01:00.9396734Z\",\n" +
                "      \"asset_id_quote\": \"BNB\",\n" +
                "      \"rate\": 439.37696346580548781827368791\n" +
                "    }\n" +
                "  ]\n" +
                "}"));
        when(currencyGetService.mapDataToResponse(Mockito.any())).thenCallRealMethod();
    }

    private ResponseEntity<?> getCurrencyExchange() {
        return currencyController.getCurrencyExchange("BTC", List.of("BNB"));
    }

    private GetResponseDto expectedResult() {
        RateDto rateDto = new RateDto("BNB", new BigDecimal("439.37696346580548781827368791"));
        GetResponseDto getResponseDto = new GetResponseDto();
        getResponseDto.setRates(List.of(rateDto));
        getResponseDto.setSource("BTC");
        return getResponseDto;

    }
}