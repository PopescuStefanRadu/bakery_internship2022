package ro.esolutions.bakery.product;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort;
import ro.esolutions.bakery.constraints.RNGesus;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FilterModel {
    @Pattern(regexp = "\\D*", message = "nu introduceti cifre in nume")
    String nameLike;
    BigDecimal priceGreaterThan;
    BigDecimal priceLessThan;

    @RNGesus
    Integer pageNumber = 0;
    @Max(1000)
    Integer pageSize = 5;
    String orderBy;
    Sort.Direction direction;
}
