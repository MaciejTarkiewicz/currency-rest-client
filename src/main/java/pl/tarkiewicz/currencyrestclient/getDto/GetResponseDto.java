package pl.tarkiewicz.currencyrestclient.getDto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import pl.tarkiewicz.currencyrestclient.postDto.ResponseDto;

@Data
public class GetResponseDto implements ResponseDto {

    @JsonAlias("asset_id_base")
    String source;
    List<RateDto> rates;

}
