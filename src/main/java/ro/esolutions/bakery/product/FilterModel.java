package ro.esolutions.bakery.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FilterModel {
    String nameLike;
    BigDecimal priceGreaterThan;
    BigDecimal priceLessThan;

    Integer pageNumber = 0;
    Integer pageSize = 5;
    String orderBy;
    Sort.Direction direction;
}
