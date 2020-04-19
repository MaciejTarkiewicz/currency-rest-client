package pl.tarkiewicz.currencyrestclient.getDto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class GetResponseDto {

    @JsonAlias("asset_id_base")
    String source;
    List<RateDto> rates;

}
