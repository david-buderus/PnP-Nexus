package de.pnp.manager.validation;

import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.upgrade.effect.SimpleItemEffect;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for item effects.
 */
public class MatchingItemEffectsValidator implements ConstraintValidator<MatchingItemEffects, Item> {

    @Override
    public boolean isValid(Item item, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (item.getEffects() == null || item.getEffects().isEmpty()) {
            return true;
        }
        if (item.getClass() != Item.class) {
            return true;
        }
        if (item.getEffects().stream().allMatch(effect -> effect instanceof SimpleItemEffect)) {
            return true;
        }

        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode("effects").addConstraintViolation();
        return false;
    }
}