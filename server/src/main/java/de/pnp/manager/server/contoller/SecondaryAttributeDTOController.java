package de.pnp.manager.server.contoller;

import de.pnp.manager.component.attributes.SecondaryAttribute;
import de.pnp.manager.component.attributes.SecondaryAttributeDTO;
import de.pnp.manager.component.math.BinaryExpressionTree;
import de.pnp.manager.component.math.IExpressionVariable;
import de.pnp.manager.component.math.IExpressionVariable.PrimaryAttributeVariable;
import de.pnp.manager.component.math.IExpressionVariable.StringVariable;
import de.pnp.manager.component.math.IllegalFormulaException;
import de.pnp.manager.exception.InvalidRequestBodyException;
import de.pnp.manager.server.database.attributes.PrimaryAttributeRepository;
import de.pnp.manager.server.database.attributes.SecondaryAttributeRepository;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bson.types.ObjectId;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A controller to convert {@link SecondaryAttributeDTO} and {@link SecondaryAttribute}.
 */
@Component
public class SecondaryAttributeDTOController {

    /**
     * All string variables allowed in {@link SecondaryAttribute}.
     */
    public static final Set<String> ALLOWED_STRING_VARIABLES = Set.of("LVL");


    @Autowired
    private PrimaryAttributeRepository primaryAttributeRepository;

    @Autowired
    private SecondaryAttributeRepository secondaryAttributeRepository;

    /**
     * Returns all {@link SecondaryAttributeDTO} of the given universe
     */
    public Collection<SecondaryAttributeDTO> getAll(String universe) {
        return getAll(universe, null);
    }

    /**
     * Returns all {@link SecondaryAttributeDTO} of the given universe matching the given ids
     */
    public Collection<SecondaryAttributeDTO> getAll(String universe, @Nullable List<ObjectId> ids) {
        Collection<SecondaryAttribute> attributes;
        if (ids == null || ids.isEmpty()) {
            attributes = secondaryAttributeRepository.getAll(universe);
        } else {
            attributes = secondaryAttributeRepository.getAll(universe, ids);
        }

        return attributes.stream().map(SecondaryAttributeDTO::from).toList();
    }

    /**
     * Inserts the given {@link SecondaryAttributeDTO} into the universe.
     */
    public Collection<SecondaryAttributeDTO> insertAll(String universe,
        List<SecondaryAttributeDTO> attributes) {
        return secondaryAttributeRepository.insertAll(universe, convert(universe, attributes)).stream()
            .map(SecondaryAttributeDTO::from).toList();
    }

    /**
     * Inserts the given {@link SecondaryAttributeDTO} into the universe.
     */
    public SecondaryAttributeDTO insert(String universe, SecondaryAttributeDTO attribute) {
        return insertAll(universe, List.of(attribute)).stream().findFirst().orElseThrow();
    }

    /**
     * Updates the given {@link SecondaryAttributeDTO}
     */
    public SecondaryAttributeDTO update(String universe, SecondaryAttributeDTO attribute) {
        Set<IExpressionVariable> attributeVariables = getPrimaryAttributeVariables(universe);

        SecondaryAttribute secondaryAttribute = new SecondaryAttribute(attribute.id(), attribute.name(),
            attribute.consumable(), createExpression(attribute.calculationFormula(), attributeVariables));

        if (checkUsedVariables(secondaryAttribute)) {
            throw new InvalidRequestBodyException("calculationFormula", "expression.unknownVariable");
        }

        return SecondaryAttributeDTO.from(secondaryAttributeRepository.update(universe, secondaryAttribute));
    }

    /**
     * Returns all supported variables of the given universe.
     */
    public List<String> getSupportedVariables(String universe) {
        return Stream.concat(getPrimaryAttributeVariables(universe).stream().map(IExpressionVariable::getIdentifier),
            ALLOWED_STRING_VARIABLES.stream()).toList();
    }

    /**
     * Converts the {@link SecondaryAttributeDTO} to {@link SecondaryAttribute}.
     */
    public List<SecondaryAttribute> convert(String universe, List<SecondaryAttributeDTO> attributeDTOS) {
        Set<IExpressionVariable> attributeVariables = getPrimaryAttributeVariables(universe);

        List<SecondaryAttribute> secondaryAttributes = attributeDTOS.stream().map(
            dto -> new SecondaryAttribute(dto.id(), dto.name(), dto.consumable(),
                createExpression(dto.calculationFormula(), attributeVariables))).toList();

        Map<String, String> violations = new HashMap<>();

        for (int i = 0; i < secondaryAttributes.size(); i++) {
            if (checkUsedVariables(secondaryAttributes.get(i))) {
                violations.put("insertAll.objects[" + i + "].calculationFormula", "expression.unknownVariable");
            }
        }

        if (!violations.isEmpty()) {
            throw new InvalidRequestBodyException(violations);
        }

        return secondaryAttributes;
    }

    /**
     * Converts the {@link SecondaryAttributeDTO} to {@link SecondaryAttribute} without any soundness checks.
     * <p>
     * Contains {@link null} for DTOs which can not be converted.
     */
    public List<@Nullable SecondaryAttribute> convertRaw(String universe, List<SecondaryAttributeDTO> attributeDTOS) {
        Set<IExpressionVariable> attributeVariables = getPrimaryAttributeVariables(universe);

        return attributeDTOS.stream().map(
            dto -> {
                try {
                    return new SecondaryAttribute(dto.id(), dto.name(), dto.consumable(),
                        BinaryExpressionTree.from(dto.calculationFormula(), attributeVariables));
                } catch (IllegalFormulaException e) {
                    return null;
                }
            }).toList();
    }

    private static boolean checkUsedVariables(SecondaryAttribute secondaryAttribute) {
        return secondaryAttribute.getCalculationFormula().getVariables().stream()
            .filter(StringVariable.class::isInstance)
            .anyMatch(v -> !ALLOWED_STRING_VARIABLES.contains(((StringVariable) v).variable()));
    }

    private Set<IExpressionVariable> getPrimaryAttributeVariables(String universe) {
        return primaryAttributeRepository.getAll(universe).stream().map(PrimaryAttributeVariable::new)
            .collect(Collectors.toSet());
    }

    private BinaryExpressionTree createExpression(String formula, Set<IExpressionVariable> variables) {
        try {
            return BinaryExpressionTree.from(formula, variables);
        } catch (IllegalFormulaException e) {
            throw new IllegalStateException(e);
        }
    }
}
