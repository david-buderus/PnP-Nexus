package de.pnp.manager.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker interface for {@link IsConsumableValidator}.
 */
@Documented
@Constraint(validatedBy = IsConsumableValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsConsumable {

    /**
     * Used in the spring backend.
     */
    String message() default "{character.attribute.isConsumable}";

    /**
     * Used in the spring backend.
     */
    Class<?>[] groups() default {};

    /**
     * Used in the spring backend.
     */
    Class<? extends Payload>[] payload() default {};
}
