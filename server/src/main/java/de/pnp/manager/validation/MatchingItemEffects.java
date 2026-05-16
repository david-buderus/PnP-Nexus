package de.pnp.manager.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Marker interface for {@link MatchingItemEffectsValidator}.
 */
@Documented
@Constraint(validatedBy = MatchingItemEffectsValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MatchingItemEffects {

    /**
     * Used in the spring backend.
     */
    String message() default "{item.effects.matches}";

    /**
     * Used in the spring backend.
     */
    Class<?>[] groups() default {};

    /**
     * Used in the spring backend.
     */
    Class<? extends Payload>[] payload() default {};
}
