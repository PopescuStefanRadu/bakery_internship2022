package ro.esolutions.bakery.constraints;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ro.esolutions.bakery.product.PatchModel;
import ro.esolutions.bakery.product.Repository;

@Component
@AllArgsConstructor
public class EntityExistsValidator implements Validator {
    private final Repository repository;

    @Override
    public boolean supports(Class<?> clazz) {
        // TODO verify that the class has a field or accessors annotated with @Id

        return PatchModel.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PatchModel p = (PatchModel) target;
        if (p.getId() == null) {
            // we're happy if we don't have an ID because really we don't want patches to have an id in the request body
            // we have them as path parameters
            return;
        }
        if (!repository.existsById(p.getId())) {
            errors.reject("errCode", "errMessage");
        }
    }
}
