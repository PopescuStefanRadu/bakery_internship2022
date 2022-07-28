package ro.esolutions.bakery.product;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class PatchModel {
    private String name;
    @Builder.Default
    private Boolean clearName = Boolean.FALSE;
    private BigDecimal price;
    @Builder.Default()
    private Boolean clearPrice = Boolean.FALSE;
}
