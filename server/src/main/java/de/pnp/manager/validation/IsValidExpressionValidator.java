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
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Validator for {@link SecondaryAttribute}.
 */
public class IsValidExpressionValidator implements ConstraintValidator<IsValidExpression, String> {

    /**
     * All string variables allowed in {@link SecondaryAttribute}.
     */
    public static final Set<EReservedVariables> RESERVED_SECONDARY_ATTRIBUTE_STRING_VARIABLES = Set.of(EReservedVariables.LEVEL, EReservedVariables.TIER);

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
            ObjectId universe = getUniverse().orElse(null);
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

    private boolean validateSecondaryAttributeExpression(ObjectId universe, String formula)
            throws IllegalFormulaException {
        Set<IExpressionVariable> variables = getPrimaryAttributeVariables(universe);
        BinaryExpressionTree expression = BinaryExpressionTree.from(formula, variables);

        return expression.getVariables().stream()
                .filter(StringVariable.class::isInstance)
                .allMatch(v -> RESERVED_SECONDARY_ATTRIBUTE_STRING_VARIABLES
                        .contains(EReservedVariables.of(((StringVariable) v).variable()).orElse(null)));
    }

    private Set<IExpressionVariable> getPrimaryAttributeVariables(ObjectId universe) {
        return primaryAttributeRepository.getAll(universe).stream()
                .map(PrimaryAttributeVariable::new).collect(
                        Collectors.toSet());
    }

    /**
     * Returns the corresponding universe for this validation.
     */
    private Optional<ObjectId> getUniverse() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            return Optional.empty();
        }

        if (!(attributes.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE,
                RequestAttributes.SCOPE_REQUEST) instanceof Map<?, ?> pathVariables)) {
            return Optional.empty();
        }

        Object universePath = pathVariables.get("universe");
        if (universePath instanceof ObjectId s) {
            return Optional.of(s);
        }
        if (universePath instanceof String s && ObjectId.isValid(s)) {
            return Optional.of(new ObjectId(s));
        }

        return Optional.empty();
    }
}
