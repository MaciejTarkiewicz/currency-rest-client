package pl.tarkiewicz.currencyrestclient;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public class CurrencyService {

    @Value("${api.key}")
    private String key;

    private static final String URL = "https://rest.coinapi.io/v1/exchangerate/";

    public HttpEntity<String> prepareHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-CoinAPI-Key", key);
        return new HttpEntity<>(headers);
    }

    public String createUrl(String currency, List<String> filter) {
        if (filter == null || filter.isEmpty()) {
            return URL + currency;
        } else {
            return URL + currency + "?filter_asset_id=" + String.join(",", filter);
        }
    }

}
