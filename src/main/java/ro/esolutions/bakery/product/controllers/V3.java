package ro.esolutions.bakery.product.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import ro.esolutions.bakery.product.FilterModel;
import ro.esolutions.bakery.product.Product;
import ro.esolutions.bakery.product.Service;

import java.util.List;

// ATENTIE acesta nu este cod 100% idiomatic de java
// Motivul pentru care pun V3 in log de V3Controller este pentru ca vreau sa evit smurf naming
// https://devcards.io/smurf-naming-convention
// totusi in java exista o mica problema:
// https://softwareengineering.stackexchange.com/questions/191929/the-problems-with-avoiding-smurf-naming-classes-with-namespaces
// Poate in viitor va fi rezolvata problema aceasta prin folosirea unei parti de FQDN(fully qualified domain name)
@RestController
@AllArgsConstructor
public class V3 {
    private final Service service;

    // This is how it looks without Specification (JPA criteria API)
    @GetMapping("/v3/products")
    public ResponseEntity<List<Product>> GetAllFiltered(@ModelAttribute FilterModel filter) {
        return ResponseEntity.ok(service.getAllFilteredV3(filter));
    }
}
