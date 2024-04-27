package de.pnp.manager.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker interface for {@link ValidCurrentPasswordValidator}.
 */
@Documented
@Constraint(validatedBy = ValidCurrentPasswordValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCurrentPassword {

    /**
     * Used in the spring backend.
     */
    String message() default "{user.currentPassword.invalid}";

    /**
     * Used in the spring backend.
     */
    Class<?>[] groups() default {};

    /**
     * Used in the spring backend.
     */
    Class<? extends Payload>[] payload() default {};
}
