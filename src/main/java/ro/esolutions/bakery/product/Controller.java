package ro.esolutions.bakery.product;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.esolutions.bakery.ValidationErrors;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

@RestController
@AllArgsConstructor
public class Controller {

    private final Repository productsRepo;
    private final Service service;

    @ExceptionHandler
    public ResponseEntity<ValidationErrors> handle(DataAccessException ex) {
        return ResponseEntity.badRequest()
                .body(ValidationErrors.builder()
                        .errorsByFieldName(Collections.emptyMap())
                        .globalErrors(List.of(ex.getMessage()))
                        .build()
                );
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> GetAll(@RequestParam(required = false) String orderBy) {
        Sort sort = orderBy != null ? Sort.by(orderBy).descending() : Sort.unsorted();
        return ResponseEntity.ok(productsRepo.findAll(sort));
    }

    public static <T> Specification<T> getSpecIfNotNull(Supplier<?> supplier, Specification<T> spec) {
        return supplier.get() == null ? Specification.where(null) : spec;
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> GetById(@PathVariable String id) {
        Product product = productsRepo.findById(id).orElseThrow(() -> new DataRetrievalFailureException("could not find product with id: %s".formatted(id)));
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
        Product product = productsRepo.getReferenceById(id);
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

        Product body = service.deleteById(id);
        return ResponseEntity.ok(body);
    }
}
