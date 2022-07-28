package ro.esolutions.bakery;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
//@NoArgsConstructor
//@Builder
public class Product {
    final String id;
    final String name;
    final BigDecimal price;

    static class Builder {
        String id;
        String name;
        BigDecimal price;

        private Builder() {}

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Product build() {
            return new Product(this.id, this.name, this.price);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
