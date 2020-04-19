package pl.tarkiewicz.currencyrestclient.postDto;

import java.util.List;

import lombok.Data;

@Data
public class Command {

    String from;
    List<String> to;
    Integer amount;

}
