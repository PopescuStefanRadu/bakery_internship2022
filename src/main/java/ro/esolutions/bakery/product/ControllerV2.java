package ro.esolutions.bakery.product;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import ro.esolutions.bakery.ValidationErrors;

import java.util.List;
import java.util.Optional;

import static ro.esolutions.bakery.product.Controller.getSpecIfNotNull;

@RestController
@AllArgsConstructor
public class ControllerV2 {
    private final Repository productsRepo;

    @GetMapping("/v2/products")
    public ResponseEntity<Object> GetAllFiltered(@ModelAttribute @Validated FilterModel filter, BindingResult validation) {
        if (validation.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationErrors.fromBindingResult(validation));
        }

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

        return ResponseEntity.ok(productsRepo.findAll(Specification.allOf(specs), pageRequest));
    }
}
