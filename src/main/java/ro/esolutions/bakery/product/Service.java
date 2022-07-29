package ro.esolutions.bakery.product;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static ro.esolutions.bakery.product.Controller.getSpecIfNotNull;

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

    public List<Product> findAll(Sort sort) {
        return productsRepo.findAll(sort);
    }

    public Product getById(String id) {
        return productsRepo.findById(id).orElseThrow(() -> new DataRetrievalFailureException("could not find product with id: %s".formatted(id)));
    }

    public Product create(Product model) {
        Product newProduct = Product.builder()
                .id(UUID.randomUUID().toString())
                .price(model.getPrice())
                .name(model.getName())
                .build();
        return productsRepo.save(newProduct);
    }

    public Product updateProduct(PatchModel model, String id) {
        Product product = productsRepo.getReferenceById(id);
        if (model.getClearName()) {
            product.setName(null);
        } else if(model.getName() != null) {
            product.setName(model.getName());
        }

        if (model.getClearPrice()) {
            product.setPrice(null);
        } else if (model.getPrice() != null) {
            product.setPrice(model.getPrice());
        }
        return productsRepo.save(product);
    }

    public Page<Product> getAllFilteredV2(FilterModel filter) {
        Specification<Product> nameLike = getSpecIfNotNull(filter::getNameLike, (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + filter.getNameLike().toLowerCase() + "%"));
        Specification<Product> priceGt = getSpecIfNotNull(filter::getPriceGreaterThan, (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("price"), filter.getPriceGreaterThan())); // gt("price", filter::getPriceGreaterThan)
        Specification<Product> priceLt = getSpecIfNotNull(filter::getPriceLessThan, (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("price"), filter.getPriceLessThan()));


        List<Specification<Product>> specs = List.of(nameLike, priceGt, priceLt);
        Integer pageNumber = Optional.ofNullable(filter.getPageNumber()).orElse(0);
        Integer pageSize = Optional.ofNullable(filter.getPageSize()).orElse(0);
        Sort ordered = Optional.ofNullable(filter.getOrderBy()).map(Sort::by).orElse(Sort.unsorted());
        Sort sorted = filter.getDirection() == null
                ? ordered
                : filter.getDirection().equals(Sort.Direction.ASC) ? ordered.ascending() : ordered.descending();

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sorted);

        Page<Product> all = productsRepo.findAll(Specification.allOf(specs), pageRequest);
        return all;
    }

    public List<Product> getAllFilteredV3(FilterModel filter) {
        String nameLike = "%" + filter.getNameLike() + "%";
        if (filter.getNameLike() != null && filter.getPriceGreaterThan() != null && filter.getPriceLessThan() != null) {
            return productsRepo.findAllByNameLikeIgnoreCaseAndPriceGreaterThanAndPriceLessThan(nameLike, filter.getPriceGreaterThan(), filter.getPriceLessThan());
        }

        if (filter.getNameLike() != null && filter.getPriceLessThan() != null) {
            return productsRepo.findAllByNameLikeAndPriceLessThan(nameLike, filter.getPriceLessThan());
        }

        if (filter.getNameLike() != null && filter.getPriceGreaterThan() != null) {
            return productsRepo.findAllByNameLikeIgnoreCaseAndPriceGreaterThan(nameLike, filter.getPriceGreaterThan());
        }

        if (filter.getPriceGreaterThan() != null && filter.getPriceLessThan() != null) {
            return productsRepo.findAllByPriceGreaterThanAndPriceLessThan(filter.getPriceGreaterThan(), filter.getPriceLessThan());
        }

        if (filter.getNameLike() != null) {
            return productsRepo.findAllByNameLikeIgnoreCase(nameLike);
        }


        if (filter.getPriceGreaterThan() != null) {
            return productsRepo.findAllByPriceGreaterThan(filter.getPriceGreaterThan());
        }

        if (filter.getPriceLessThan() != null) {
            return productsRepo.findAllByPriceLessThan(filter.getPriceLessThan());
        }

        return productsRepo.findAll();
    }

}
