package ro.esolutions.bakery.product;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class Controller {

    private final Repository productsRepo;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> GetAll() {
        return ResponseEntity.ok(productsRepo.findAll());
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> GetById(@PathVariable String id) {
        Product product = productsRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(product);
    }

    @PostMapping(path = "/product/create")
    public ResponseEntity<Object> Create(@RequestBody Product model) {
        Product newProduct = Product.builder()
                .id(UUID.randomUUID().toString())
                .price(model.getPrice())
                .name(model.getName())
                .build();
        productsRepo.save(newProduct);
        return ResponseEntity.ok(null);
    }

    @PatchMapping(path = "/product/{id}")
    public ResponseEntity<Product> Update(@RequestBody PatchModel model, @PathVariable String id) {
        Product product = productsRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));


        if (model.getClearName()) {
            product.setName(null);
        } else if(model.getName() != null) {
            product.setName(model.getName());
        }

        if (model.getClearPrice()) {
            product.setPrice(null);
        } else if(model.getPrice() != null) {
            product.setPrice(model.getPrice());
        }
        return ResponseEntity.ok(productsRepo.save(product));
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<Product> Delete(@PathVariable("id") String id) {
        Product productToRemove = productsRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        productsRepo.delete(productToRemove);
        return ResponseEntity.ok(productToRemove);
    }
}
