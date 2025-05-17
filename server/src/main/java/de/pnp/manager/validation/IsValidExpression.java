package de.pnp.manager.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Marker interface for {@link IsValidExpressionValidator}.
 */
@Documented
@Constraint(validatedBy = IsValidExpressionValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsValidExpression {

    /**
     * Used in the spring backend.
     */
    String message() default "{expression.invalid}";

    /**
     * The type of expression.
     */
    EExpressionType expressionType();

    /**
     * Used in the spring backend.
     */
    Class<?>[] groups() default {};

    /**
     * Used in the spring backend.
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * The different types of expressions.
     */
    enum EExpressionType {
        SECONDARY_ATTRIBUTE_EXPRESSION
    }
}
