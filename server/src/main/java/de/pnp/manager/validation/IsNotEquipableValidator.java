package de.pnp.manager.validation;

import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.interfaces.IEquipableItem;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for {@link Item}.
 */
public class IsNotEquipableValidator implements ConstraintValidator<IsNotEquipable, Item> {

    @Override
    public boolean isValid(Item value, ConstraintValidatorContext context) {
        return !(value instanceof IEquipableItem);
    }
}
