package ro.esolutions.bakery.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.math.BigDecimal;
import java.util.List;

public interface Repository extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {

    List<Product> findAllByNameLikeIgnoreCase(String name);


    List<Product> findAllByNameLikeIgnoreCaseAndPriceGreaterThan(String name, BigDecimal price);

    List<Product> findAllByNameLikeIgnoreCaseAndPriceGreaterThanAndPriceLessThan(String name, BigDecimal priceGt, BigDecimal priceLt);

    List<Product> findAllByPriceGreaterThan(BigDecimal priceGreaterThan);

    List<Product> findAllByPriceLessThan(BigDecimal priceLessThan);

    List<Product> findAllByNameLikeAndPriceLessThan(String nameLike, BigDecimal priceLessThan);

    List<Product> findAllByPriceGreaterThanAndPriceLessThan(BigDecimal priceGreaterThan, BigDecimal priceLessThan);
}
