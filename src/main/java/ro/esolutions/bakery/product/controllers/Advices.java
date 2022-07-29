package ro.esolutions.bakery.product.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ro.esolutions.bakery.ValidationErrors;

@RestControllerAdvice(basePackages = {"ro.esolutions.bakery.product.controllers"})
public class Advices {
    @ExceptionHandler()
    public ResponseEntity<ValidationErrors> handleBindingExceptions(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body(ValidationErrors.fromBindingResult(ex));
    }
}
