package ro.esolutions.bakery.product;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class ControllerV3 {
    private final Service service;

    // This is how it looks without Specification (JPA criteria API)
    @GetMapping("/v3/products")
    public ResponseEntity<List<Product>> GetAllFiltered(@ModelAttribute FilterModel filter) {
        return ResponseEntity.ok(service.getAllFilteredV3(filter));
    }
}
