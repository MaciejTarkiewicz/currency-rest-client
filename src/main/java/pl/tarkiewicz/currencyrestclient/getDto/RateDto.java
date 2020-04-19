package pl.tarkiewicz.currencyrestclient.getDto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Data
@NoArgsConstructor
public class RateDto {

    @JsonAlias("asset_id_quote")
    String name;
    BigDecimal rate;
}
