package ro.esolutions.bakery.product;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Service {
    private final Repository productsRepo;

    public Product deleteById(String id) {
        Product productToRemove = productsRepo.getReferenceById(id);
        productsRepo.delete(productToRemove);
        return productToRemove;
    }
}
