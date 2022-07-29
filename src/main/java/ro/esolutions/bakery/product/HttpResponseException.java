package ro.esolutions.bakery.product;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public class HttpResponseException extends RuntimeException {
    final HttpStatus status;
    final Object response;
}
