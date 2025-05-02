package de.pnp.manager.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.validation.ObjectError;

/**
 * A DTO which contains validation errors
 */
public class ValidationErrorResponse {

    private final Map<String, Object> errors;

    public ValidationErrorResponse() {
        errors = new HashMap<>();
    }

    /**
     * Adds a validation error to the DTO.
     */
    public void addValidationError(String key, String error) {
        errors.put(key, error);
    }

    public Map<String, Object> getErrors() {
        return errors;
    }

    /**
     * Create a {@link ValidationErrorResponse} from the given violations.
     */
    public static ValidationErrorResponse fromViolations(Collection<ConstraintViolation<?>> violations) {
        ValidationErrorResponse response = new ValidationErrorResponse();

        for (ConstraintViolation<?> violation : violations) {
            response.addValidationError(transformPropertyPath(violation.getPropertyPath()), violation.getMessage());
        }

        return response;
    }

    /**
     * Create a {@link ValidationErrorResponse} from the given violations.
     */
    public static ValidationErrorResponse fromErrors(Collection<ObjectError> errors) {
        ValidationErrorResponse response = new ValidationErrorResponse();

        for (ObjectError error : errors) {
            if (!error.contains(ConstraintViolation.class)) {
                continue;
            }

            ConstraintViolation<?> violation = error.unwrap(ConstraintViolation.class);
            response.addValidationError(transformPropertyPath(violation.getPropertyPath()), violation.getMessage());
        }

        return response;
    }

    private static String transformPropertyPath(Path path) {
        return StreamSupport.stream(path.spliterator(), false).map(node -> {
            if (node.getIndex() != null) {
                return node.getIndex() + "." + node.getName();
            } else {
                return node.getName();
            }
        }).collect(Collectors.joining("."));
    }
}
