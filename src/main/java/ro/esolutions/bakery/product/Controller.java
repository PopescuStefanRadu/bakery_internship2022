package ro.esolutions.bakery.product;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ro.esolutions.bakery.ValidationErrors;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

@RestController
@AllArgsConstructor
public class Controller {
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
        return ResponseEntity.ok(service.findAll(sort));
    }

    public static <T> Specification<T> getSpecIfNotNull(Supplier<?> supplier, Specification<T> spec) {
        return supplier.get() == null ? Specification.where(null) : spec;
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> GetById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping(path = "/product/create")
    public ResponseEntity<Product> Create(
            @RequestBody @Validated({Product.CreateModelConstraints.class, jakarta.validation.groups.Default.class}) Product model
    ) {
        return ResponseEntity.ok(service.create(model));
    }


    @PatchMapping(path = "/product/{id}")
    public ResponseEntity<Product> Update(@RequestBody PatchModel model, @PathVariable String id) {
        return ResponseEntity.ok(service.updateProduct(model, id));
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<Product> Delete(@PathVariable("id") String id) {

        Product body = service.deleteById(id);
        return ResponseEntity.ok(body);
    }
}
