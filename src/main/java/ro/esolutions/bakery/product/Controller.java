package ro.esolutions.bakery.product;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@RestController
@AllArgsConstructor
public class Controller {

    private final Repository productsRepo;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> GetAll(@RequestParam(required = false) String orderBy) {
        Sort sort = orderBy != null ? Sort.by(orderBy).descending() : Sort.unsorted();
        return ResponseEntity.ok(productsRepo.findAll(sort));
    }

    static <T> Specification<T> getSpecIfNotNull(Supplier<?> supplier, Specification<T> spec) {
        return supplier.get() == null ? Specification.where(null) : spec;
    }

    @GetMapping("/v2/products")
    public ResponseEntity<Page<Product>> GetAllFiltered(@ModelAttribute FilterModel filter) {
        Specification<Product> nameLike = getSpecIfNotNull(filter::getNameLike, (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + filter.getNameLike().toLowerCase() + "%"));
        Specification<Product> priceGt = getSpecIfNotNull(filter::getPriceGreaterThan, (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("price"), filter.getPriceGreaterThan())); // gt("price", filter::getPriceGreaterThan)
        Specification<Product> priceLt = getSpecIfNotNull(filter::getPriceLessThan, (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("price"), filter.getPriceLessThan()));


        List<Specification<Product>> specs = List.of(nameLike, priceGt, priceLt);
        Integer pageNumber = Optional.ofNullable(filter.getPageNumber()).orElse(0);
        Integer pageSize = Optional.ofNullable(filter.getPageSize()).orElse(0);
        Sort ordered = Optional.ofNullable(filter.getOrderBy()).map(Sort::by).orElse(Sort.unsorted());
        Sort sorted = filter.getDirection() == null
                ? ordered
                : filter.getDirection().equals(Sort.Direction.ASC) ? ordered.ascending() : ordered.descending();

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sorted);

        Pageable page = filter.getPageSize() == null ?  Pageable.ofSize(1000) : Pageable.ofSize(filter.getPageSize());

        return ResponseEntity.ok(productsRepo.findAll(Specification.allOf(specs), pageRequest));
    }

    // This is how it looks without Specification (JPA criteria API)
    @GetMapping("/v3/products")
    public ResponseEntity<List<Product>> GetAllFiltered2(@ModelAttribute FilterModel filter) {
        String nameLike = "%" + filter.getNameLike() + "%";
        if (filter.getNameLike() != null && filter.getPriceGreaterThan() != null && filter.getPriceLessThan() != null) {
            return ResponseEntity.ok(productsRepo.findAllByNameLikeIgnoreCaseAndPriceGreaterThanAndPriceLessThan(nameLike, filter.getPriceGreaterThan(), filter.getPriceLessThan()));
        }

        // TODO (name,lt,gt)(name,lt)(name, gt)(lt,gt)(lt)(gt)(name)
        if (filter.getNameLike() != null && filter.getPriceLessThan() != null) {
            return ResponseEntity.ok(productsRepo.findAllByNameLikeAndPriceLessThan(nameLike, filter.getPriceLessThan()));
        }


        if (filter.getNameLike() != null && filter.getPriceGreaterThan() != null) {
            return ResponseEntity.ok(productsRepo.findAllByNameLikeIgnoreCaseAndPriceGreaterThan(nameLike, filter.getPriceGreaterThan()));
        }

        if (filter.getPriceGreaterThan() != null && filter.getPriceLessThan() != null) {
            return ResponseEntity.ok(productsRepo.findAllByPriceGreaterThanAndPriceLessThan(filter.getPriceGreaterThan(), filter.getPriceLessThan()));
        }

        if (filter.getNameLike() != null) {
            return ResponseEntity.ok(productsRepo.findAllByNameLikeIgnoreCase(nameLike));
        }


        if (filter.getPriceGreaterThan() != null) {
            return ResponseEntity.ok(productsRepo.findAllByPriceGreaterThan(filter.getPriceGreaterThan()));
        }

        if (filter.getPriceLessThan() != null) {
            return ResponseEntity.ok(productsRepo.findAllByPriceLessThan(filter.getPriceLessThan()));
        }

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
