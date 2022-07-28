package ro.esolutions.bakery.constraints;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.NotNull;

import java.util.Random;

public class RNGesusValidator implements ConstraintValidator<RNGesus, Integer> {
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        var random = new Random();
        int i = random.nextInt(3);
        return value.equals(i);
    }
}
