package de.pnp.manager.validation;

import de.pnp.manager.component.item.Item;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for stack sizes.
 */
public class MatchingStackSizesValidator implements ConstraintValidator<MatchingStackSizes, Item> {

    @Override
    public boolean isValid(Item item, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (item.getMaximumStackSize() >= item.getMinimumStackSize()) {
            return true;
        }
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
            .addPropertyNode("maximumStackSize").addConstraintViolation();
        return false;
    }
}
