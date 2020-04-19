package pl.tarkiewicz.currencyrestclient;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseDto {

    String from;

    Map<String, CurrencyDto> currencyDtos;

}
