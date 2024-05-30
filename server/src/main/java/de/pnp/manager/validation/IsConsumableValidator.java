package de.pnp.manager.validation;

import de.pnp.manager.component.attributes.SecondaryAttribute;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for {@link SecondaryAttribute}.
 */
public class IsConsumableValidator implements ConstraintValidator<IsConsumable, SecondaryAttribute> {

    @Override
    public boolean isValid(SecondaryAttribute value, ConstraintValidatorContext context) {
        return value != null && value.isConsumable();
    }
}
