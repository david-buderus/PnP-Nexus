package de.pnp.manager.validation;

import de.pnp.manager.component.attributes.SecondaryAttribute;
import de.pnp.manager.component.math.BinaryExpressionTree;
import de.pnp.manager.component.math.IllegalFormulaException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 * Validator for {@link SecondaryAttribute}.
 */
public class IsValidExpressionValidator implements ConstraintValidator<IsValidExpression, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(value)) {
            return false;
        }
        try {
            BinaryExpressionTree.from(value, Set.of());
            return true;
        } catch (IllegalFormulaException e) {
            return false;
        }
    }
}
