package de.pnp.manager.exception;

import java.util.List;
import java.util.Set;

/**
 * An exception that indicates that objects which should be inserted into the database does not validate.
 */
public class ValidationException extends RuntimeException {

    private final List<Set<String>> notValidatesFields;

    public ValidationException(List<Set<String>> notValidatesFields) {
        this.notValidatesFields = notValidatesFields;
    }

    public List<Set<String>> getNotValidatesFields() {
        return notValidatesFields;
    }
}
