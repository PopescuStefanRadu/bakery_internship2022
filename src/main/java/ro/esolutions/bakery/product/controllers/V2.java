package ro.esolutions.bakery.product.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import ro.esolutions.bakery.ValidationErrors;
import ro.esolutions.bakery.product.FilterModel;
import ro.esolutions.bakery.product.Service;

@RestController
@AllArgsConstructor
public class V2 {
    private final Service service;

    @GetMapping("/v2/products")
    public ResponseEntity<Object> GetAllFiltered(@ModelAttribute @Validated FilterModel filter, BindingResult validation) {
        if (validation.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationErrors.fromBindingResult(validation));
        }

        return ResponseEntity.ok(service.getAllFilteredV2(filter));
    }


}