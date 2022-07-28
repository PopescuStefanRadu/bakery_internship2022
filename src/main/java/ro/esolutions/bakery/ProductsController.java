package ro.esolutions.bakery;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.badRequest;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @GetMapping
    public ResponseEntity<List<Product>> GetProducts() {
//        return ResponseEntity.status(HttpStatus.CREATED).body(null);
        return ResponseEntity.ok(
                List.of(
                        Product.builder()
                                .withPrice(new BigDecimal("3"))
                                .withName("Paine Franzela 2.5Lei")
                                .withId(UUID.randomUUID().toString())
                                .build())
        );
    }
}
