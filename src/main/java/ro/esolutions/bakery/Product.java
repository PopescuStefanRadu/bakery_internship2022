package ro.esolutions.bakery;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Product {
    final String id;
    final String name;
    final BigDecimal price;
}
