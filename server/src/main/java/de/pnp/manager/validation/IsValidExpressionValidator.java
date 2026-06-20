package de.pnp.manager.validation;

import de.pnp.manager.component.attributes.SecondaryAttribute;
import de.pnp.manager.component.math.BinaryExpressionTree;
import de.pnp.manager.component.math.EReservedVariables;
import de.pnp.manager.component.math.IExpressionVariable;
import de.pnp.manager.component.math.IExpressionVariable.PrimaryAttributeVariable;
import de.pnp.manager.component.math.IExpressionVariable.StringVariable;
import de.pnp.manager.component.math.IllegalFormulaException;
import de.pnp.manager.server.database.attributes.PrimaryAttributeRepository;
import de.pnp.manager.validation.IsValidExpression.EExpressionType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.stream.Collectors;

import static de.pnp.manager.component.math.EReservedVariables.*;

/**
 * Validator for {@link SecondaryAttribute}.
 */
public class IsValidExpressionValidator implements ConstraintValidator<IsValidExpression, String> {

    private final PrimaryAttributeRepository primaryAttributeRepository;

    private EExpressionType expressionType;

    public IsValidExpressionValidator(@Autowired PrimaryAttributeRepository primaryAttributeRepository) {
        this.primaryAttributeRepository = primaryAttributeRepository;
    }

    @Override
    public void initialize(IsValidExpression constraintAnnotation) {
        this.expressionType = constraintAnnotation.expressionType();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (StringUtils.isBlank(value)) {
            return false;
        }
        try {
            ObjectId universe = ValidationUtils.getUniverse().orElse(null);

            return switch (expressionType) {
                case SECONDARY_ATTRIBUTE_EXPRESSION -> validateSecondaryAttributeExpression(universe, value);
                case TIER_FORMULA -> validateExpression(value, RESERVED_TIER_STRING_VARIABLES);
                case TALENT_POINT_FORMULA -> validateExpression(value, RESERVED_TALENT_STRING_VARIABLES);
            };
        } catch (IllegalFormulaException e) {
            return false;
        }
    }

    private boolean validateSecondaryAttributeExpression(ObjectId universe, String formula)
            throws IllegalFormulaException {
        if (universe == null) {
            // We don't have access to the universe
            // We can only do a basic check
            BinaryExpressionTree.from(formula, Set.of());
            return true;
        }

        Set<IExpressionVariable> variables = getPrimaryAttributeVariables(universe);
        BinaryExpressionTree expression = BinaryExpressionTree.from(formula, variables);

        return expression.getVariables().stream()
                .filter(StringVariable.class::isInstance)
                .allMatch(v -> RESERVED_SECONDARY_ATTRIBUTE_STRING_VARIABLES
                        .contains(EReservedVariables.of(((StringVariable) v).variable()).orElse(null)));
    }

    private boolean validateExpression(String formula, Set<EReservedVariables> reserved)
            throws IllegalFormulaException {
        BinaryExpressionTree expression = BinaryExpressionTree.from(formula, Set.of());

        return expression.getVariables().stream()
                .filter(StringVariable.class::isInstance)
                .allMatch(v -> reserved.contains(
                        EReservedVariables.of(((StringVariable) v).variable()).orElse(null))
                );
    }

    private Set<IExpressionVariable> getPrimaryAttributeVariables(ObjectId universe) {
        return primaryAttributeRepository.getAll(universe).stream()
                .map(PrimaryAttributeVariable::new).collect(
                        Collectors.toSet());
    }
}
