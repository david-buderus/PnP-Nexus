package de.pnp.manager.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Marker interface for {@link ValidPnPCharacterValidator}.
 */
@Documented
@Constraint(validatedBy = ValidPnPCharacterValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPnPCharacter {

    /**
     * Used in the spring backend.
     */
    String message() default "";

    /**
     * Used in the spring backend.
     */
    Class<?>[] groups() default {};

    /**
     * Used in the spring backend.
     */
    Class<? extends Payload>[] payload() default {};
}
