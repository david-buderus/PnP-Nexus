package de.pnp.manager.validation;

import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.ItemType.ETypeRestriction;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator to check that the {@link ETypeRestriction ETypeRestriction} of {@link Item#getType()} and
 * {@link Item#getSubtype()} matches the {@link Item}.
 */
public class MatchingItemTypesValidator implements ConstraintValidator<MatchingItemTypes, Item> {

    @Override
    public boolean isValid(Item item, ConstraintValidatorContext context) {
        // Only checking the ItemType fields. No need for the default constraint violation.
        context.disableDefaultConstraintViolation();
        boolean valid = true;

        if (item.getType() != null && !item.getType().getTypeRestriction().applicableOn(item)) {
            context.buildConstraintViolationWithTemplate(getMessageTemplate(item.getType().getTypeRestriction()))
                .addPropertyNode("type").addConstraintViolation();
            valid = false;
        }
        if (item.getSubtype() != null && !item.getSubtype().getTypeRestriction().applicableOn(item)) {
            context.buildConstraintViolationWithTemplate(getMessageTemplate(item.getSubtype().getTypeRestriction()))
                .addPropertyNode("subtype").addConstraintViolation();
            valid = false;
        }

        return valid;
    }

    private String getMessageTemplate(ETypeRestriction typeRestriction) {
        return String.format("{item.type.matches.%s}", typeRestriction.getMessageTemplate());
    }
}
