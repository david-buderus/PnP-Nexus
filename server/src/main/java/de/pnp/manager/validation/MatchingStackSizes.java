package de.pnp.manager.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker interface for {@link MatchingStackSizesValidator}.
 */
@Documented
@Constraint(validatedBy = MatchingStackSizesValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MatchingStackSizes {

    /**
     * Used in the spring backend.
     */
    String message() default "item.stackSize.matches";

    /**
     * Used in the spring backend.
     */
    Class<?>[] groups() default {};

    /**
     * Used in the spring backend.
     */
    Class<? extends Payload>[] payload() default {};
}
