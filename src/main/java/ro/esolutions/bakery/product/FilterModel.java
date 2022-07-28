package ro.esolutions.bakery.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FilterModel {
    String nameLike;
    BigDecimal priceGreaterThan;
    BigDecimal priceLessThan;
}
