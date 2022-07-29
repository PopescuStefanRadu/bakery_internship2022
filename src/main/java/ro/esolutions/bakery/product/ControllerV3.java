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
    private final Repository productsRepo;

    // This is how it looks without Specification (JPA criteria API)
    @GetMapping("/v3/products")
    public ResponseEntity<List<Product>> GetAllFiltered2(@ModelAttribute FilterModel filter) {
        String nameLike = "%" + filter.getNameLike() + "%";
        if (filter.getNameLike() != null && filter.getPriceGreaterThan() != null && filter.getPriceLessThan() != null) {
            return ResponseEntity.ok(productsRepo.findAllByNameLikeIgnoreCaseAndPriceGreaterThanAndPriceLessThan(nameLike, filter.getPriceGreaterThan(), filter.getPriceLessThan()));
        }

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
}
