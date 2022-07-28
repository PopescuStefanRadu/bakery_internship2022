package ro.esolutions.bakery;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

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

    @PatchMapping(path = "/product/{id}")
    public ResponseEntity<Product> Update(@RequestBody ProductPatchModel model, @PathVariable String id) {
        Optional<Product> productToUpdate = shopProducts.stream().filter(p -> p.getId().equals(id)).findFirst();
        Product product = productToUpdate.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));


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

        return ResponseEntity.ok(product);
    }


    @DeleteMapping("/product/{id}")
    public ResponseEntity<Product> Delete(@PathVariable("id") String id) {
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
