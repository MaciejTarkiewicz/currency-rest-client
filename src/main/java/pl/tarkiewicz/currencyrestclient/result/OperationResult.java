package pl.tarkiewicz.currencyrestclient.result;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.tarkiewicz.currencyrestclient.postDto.ResponseDto;

public class OperationResult {

    public static ResponseEntity<?> success(ResponseDto message) {
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    public static ResponseEntity<?> failureRequest(String message) {
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<?> failureExternalApi() {
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

}
