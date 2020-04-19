package pl.tarkiewicz.currencyrestclient;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurrencyDto {

    BigDecimal rate;
    Integer amount;
    BigDecimal result;
    BigDecimal fee;
}
