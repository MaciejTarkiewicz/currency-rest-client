package pl.tarkiewicz.currencyrestclient;

import java.util.List;

import lombok.Data;

@Data
public class PostDto {

    String from;
    List<String> to;
    Integer amount;

}
