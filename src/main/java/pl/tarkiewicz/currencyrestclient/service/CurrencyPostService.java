package pl.tarkiewicz.currencyrestclient.service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import pl.tarkiewicz.currencyrestclient.getDto.GetResponseDto;
import pl.tarkiewicz.currencyrestclient.getDto.RateDto;
import pl.tarkiewicz.currencyrestclient.postDto.Command;
import pl.tarkiewicz.currencyrestclient.postDto.CurrencyDto;
import pl.tarkiewicz.currencyrestclient.postDto.PostResponseDto;

@Service
public class CurrencyPostService {

    private static final BigDecimal PROVISION = new BigDecimal("0.01");

    public PostResponseDto apply(Command command, GetResponseDto responseDto) {
        return PostResponseDto.builder()
                .from(responseDto.getSource())
                .to(toTargetCurrency(command, responseDto))
                .build();
    }

    private Map<String, CurrencyDto> toTargetCurrency(Command command, GetResponseDto responseDto) {
        return responseDto.getRates().parallelStream()
                .collect(Collectors.toMap(RateDto::getName
                        , mapValueCreator(command)));
    }

    private Function<RateDto, CurrencyDto> mapValueCreator(Command command) {
        return value -> {
            BigDecimal fee = value.getRate().multiply(PROVISION).multiply(new BigDecimal(command.getAmount()));
            return currencyDtoBuild(command, value, fee);
        };
    }

    private CurrencyDto currencyDtoBuild(Command command, RateDto value, BigDecimal fee) {
        return CurrencyDto.builder()
                .amount(command.getAmount())
                .result(value.getRate().multiply(new BigDecimal(command.getAmount())).add(fee))
                .fee(fee)
                .rate(value.getRate())
                .build();
    }

}
