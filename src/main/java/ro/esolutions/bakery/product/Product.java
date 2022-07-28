package ro.esolutions.bakery.product;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    private String id;
    @Pattern(regexp = ".*^[0-9].*", message = "nu introduceti cifre in nume")
    private String name;
    private BigDecimal price;
}
