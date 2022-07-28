package ro.esolutions.bakery;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ProductPatchModel {
    private String name;
    @Builder.Default
    private Boolean clearName = Boolean.FALSE;
    private BigDecimal price;
    @Builder.Default()
    private Boolean clearPrice = Boolean.FALSE;
}
