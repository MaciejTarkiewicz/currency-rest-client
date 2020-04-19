package pl.tarkiewicz.currencyrestclient.result;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.tarkiewicz.currencyrestclient.getDto.GetResponseDto;

public class GetOperationResult {

    public static ResponseEntity<?> success(GetResponseDto message) {
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    public static ResponseEntity<?> failure(String message) {
        return new ResponseEntity<>(message, HttpStatus.BAD_GATEWAY);
    }

}
