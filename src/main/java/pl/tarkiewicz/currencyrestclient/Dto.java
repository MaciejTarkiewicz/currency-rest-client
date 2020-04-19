package pl.tarkiewicz.currencyrestclient;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Dto {

    @JsonAlias("asset_id_base")
    String source;
    List<Rate> rates;

}
