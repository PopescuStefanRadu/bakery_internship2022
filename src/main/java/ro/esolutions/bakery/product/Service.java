package ro.esolutions.bakery.product;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
@Transactional
public class Service {
    private final Repository productsRepo;

    public Product deleteById(String id) {
        // https://stackoverflow.com/questions/49784730/spring-jpa-repository-cannot-catch-entitynotfoundexception
        // getOne() is just a wrapper for EntityManager.getReference(). That method will not throw any exception.
        // It returns an uninitialized proxy, assuming that the entity indeed exists. It doesn't get the entity state
        // from the database, and thus doesn't even know if it exists. It assumes it does.
        // You'll only get an exception later, if you try to access the state of the entity.
        Product productToRemove = productsRepo.findById(id).orElseThrow(() -> new DataRetrievalFailureException("could not find product with id: %s".formatted(id)));
        productsRepo.delete(productToRemove);
        return productToRemove;
    }
}
