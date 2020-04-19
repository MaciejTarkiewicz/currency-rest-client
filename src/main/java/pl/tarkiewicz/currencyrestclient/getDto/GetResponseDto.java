package pl.tarkiewicz.currencyrestclient.getDto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetResponseDto {

    @JsonAlias("asset_id_base")
    String source;
    List<RateDto> rates;

}
