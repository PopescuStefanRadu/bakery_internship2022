package ro.esolutions.bakery;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class ProductsController {

    private final List<Product> shopProducts = new ArrayList<>();


    public ProductsController() {
        shopProducts.add(Product.builder()
                .price(new BigDecimal("3"))
                .name("Paine Franzela 2.5Lei")
                .id(UUID.randomUUID().toString())
                .build());
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> GetAll() {
        return ResponseEntity.ok(shopProducts);
    }

    @PostMapping(path = "/product/create")
    public ResponseEntity<Object> Create(@RequestBody Product model) {
        Product newProduct = Product.builder()
                .id(UUID.randomUUID().toString())
                .price(model.getPrice())
                .name(model.getName())
                .build();
        shopProducts.add(newProduct);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<Product> Delete(@PathVariable("id") String id){
        Optional<Product> removedProduct = shopProducts.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
        removedProduct.ifPresent(shopProducts::remove);
        if (removedProduct.isPresent()) {
            return ResponseEntity.ok(removedProduct.get());
        }
        return ResponseEntity.notFound().build();
    }
}
