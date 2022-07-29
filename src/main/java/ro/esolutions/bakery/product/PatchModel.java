package ro.esolutions.bakery.product;

import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class PatchModel {
    // we don't really need this here, we have the Id in the path variable, this is done just for
    // example purposes for Validator interface
    private String id;
    @Pattern(regexp = "\\D*", message = "nu introduceti cifre in nume")
    private String name;
    @Builder.Default
    private Boolean clearName = Boolean.FALSE;
    private BigDecimal price;
    @Builder.Default()
    private Boolean clearPrice = Boolean.FALSE;
}
