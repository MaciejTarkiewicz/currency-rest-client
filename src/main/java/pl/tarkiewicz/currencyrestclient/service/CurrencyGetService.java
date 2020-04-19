package pl.tarkiewicz.currencyrestclient.service;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.tarkiewicz.currencyrestclient.getDto.GetResponseDto;

@Service
public class CurrencyGetService {

    @Value("${api.key}")
    private String key;

    private static final String URL = "https://rest.coinapi.io/v1/exchangerate/";

    private HttpEntity<String> prepareHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-CoinAPI-Key", key);
        return new HttpEntity<>(headers);
    }

    private String createUrl(String currency, List<String> filter) {
        if (filter == null || filter.isEmpty()) {
            return URL + currency;
        } else {
            return URL + currency + "?filter_asset_id=" + String.join(",", filter);
        }
    }

    public Optional<String> getDataFromApi(String currency, List<String> filter) {
        RestTemplate restTemplate = new RestTemplate();
        return Optional.ofNullable(restTemplate.exchange(createUrl(currency, filter), HttpMethod.GET, prepareHeaders(), String.class).getBody());
    }

    public GetResponseDto mapDataToResponse(String request) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(request, GetResponseDto.class);
    }

}
