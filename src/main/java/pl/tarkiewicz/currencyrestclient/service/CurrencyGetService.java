package pl.tarkiewicz.currencyrestclient.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    public Optional<String> prepareBody(String currency, List<String> filter) {
        RestTemplate restTemplate = new RestTemplate();
        return Optional.ofNullable(restTemplate.exchange(createUrl(currency, filter), HttpMethod.GET, prepareHeaders(), String.class).getBody());
    }

}
