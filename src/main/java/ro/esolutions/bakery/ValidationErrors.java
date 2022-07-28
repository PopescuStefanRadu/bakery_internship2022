package ro.esolutions.bakery;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Getter
@Setter
@AllArgsConstructor
public class ValidationErrors {
    final Map<String, List<String>> errorsByFieldName;
    final List<String> globalErrors;

    public static ValidationErrors fromBindingResult(BindingResult br) {
        var errorsByFieldName = new HashMap<String, List<String>>();
        br.getFieldErrors().stream().forEach(fieldError -> {
            var list = new ArrayList<String>();
            list.add(fieldError.getDefaultMessage());
            errorsByFieldName.merge(fieldError.getField(), list, (msgs1, msgs2) -> {
                msgs1.addAll(msgs2);
                return msgs1;
            });
        });

        List<String> globalErrs = br.getGlobalErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).toList();

        return new ValidationErrors(errorsByFieldName, globalErrs);
    }
}
