package pl.tarkiewicz.currencyrestclient.postDto;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PostResponseDto implements ResponseDto {

    String from;
    Map<String, CurrencyDto> to;

}
