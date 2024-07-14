package de.pnp.manager.exception;

import java.util.Map;

/**
 * @see jakarta.validation.ConstraintViolationException
 */
public class InvalidRequestBodyException extends RuntimeException {

    private final Map<String, String> violations;

    public InvalidRequestBodyException(String path, String messageKey) {
        this(Map.of(path, messageKey));
    }

    public InvalidRequestBodyException(Map<String, String> violations) {
        this.violations = violations;
    }

    public Map<String, String> getViolations() {
        return violations;
    }
}
