package de.pnp.manager.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Marker interface for {@link IsNotEquipableValidator}.
 */
@Documented
@Constraint(validatedBy = IsNotEquipableValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsNotEquipable {

    /**
     * Used in the spring backend.
     */
    String message() default "{item.not_equipable}";

    /**
     * Used in the spring backend.
     */
    Class<?>[] groups() default {};

    /**
     * Used in the spring backend.
     */
    Class<? extends Payload>[] payload() default {};
}
