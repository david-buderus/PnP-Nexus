package de.pnp.manager.validation;

import de.pnp.manager.component.attributes.SecondaryAttribute;
import de.pnp.manager.component.math.BinaryExpressionTree;
import de.pnp.manager.component.math.IExpressionVariable;
import de.pnp.manager.component.math.IExpressionVariable.PrimaryAttributeVariable;
import de.pnp.manager.component.math.IExpressionVariable.StringVariable;
import de.pnp.manager.component.math.IllegalFormulaException;
import de.pnp.manager.server.database.attributes.PrimaryAttributeRepository;
import de.pnp.manager.validation.IsValidExpression.EExpressionType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerMapping;

/**
 * Validator for {@link SecondaryAttribute}.
 */
public class IsValidExpressionValidator implements ConstraintValidator<IsValidExpression, String> {

    /**
     * All string variables allowed in {@link SecondaryAttribute}.
     */
    public static final Set<String> ALLOWED_SECONDARY_ATTRIBUTE_STRING_VARIABLES = Set.of("LVL");

    @Autowired
    private PrimaryAttributeRepository primaryAttributeRepository;

    private EExpressionType expressionType;

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
            String universe = getUniverse().orElse(null);
            if (universe == null) {
                // We don't have access to the universe
                // We can only do a basic check
                BinaryExpressionTree.from(value, Set.of());
                return true;
            }
            return switch (expressionType) {
                case SECONDARY_ATTRIBUTE_EXPRESSION -> validateSecondaryAttributeExpression(universe, value);
            };
        } catch (IllegalFormulaException e) {
            return false;
        }
    }

    private boolean validateSecondaryAttributeExpression(String universe, String formula)
        throws IllegalFormulaException {
        Set<IExpressionVariable> variables = getPrimaryAttributeVariables(universe);
        BinaryExpressionTree expression = BinaryExpressionTree.from(formula, variables);

        return expression.getVariables().stream()
            .filter(StringVariable.class::isInstance)
            .allMatch(v -> ALLOWED_SECONDARY_ATTRIBUTE_STRING_VARIABLES.contains(
                ((StringVariable) v).variable()));

    }

    private Set<IExpressionVariable> getPrimaryAttributeVariables(String universe) {
        return primaryAttributeRepository.getAll(universe).stream()
            .map(PrimaryAttributeVariable::new).collect(
                Collectors.toSet());
    }

    /**
     * Returns the corresponding universe for this validation.
     */
    private Optional<String> getUniverse() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            return Optional.empty();
        }

        if (!(attributes.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE,
            RequestAttributes.SCOPE_REQUEST) instanceof Map<?, ?> pathVariables)) {
            return Optional.empty();
        }

        if (pathVariables.get("universe") instanceof String s) {
            return Optional.of(s);
        }

        return Optional.empty();
    }
}
