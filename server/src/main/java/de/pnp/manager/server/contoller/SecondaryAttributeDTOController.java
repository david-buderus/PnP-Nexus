package de.pnp.manager.server.contoller;

import de.pnp.manager.component.attributes.SecondaryAttribute;
import de.pnp.manager.component.attributes.SecondaryAttributeDTO;
import de.pnp.manager.component.math.BinaryExpressionTree;
import de.pnp.manager.component.math.IExpressionVariable;
import de.pnp.manager.component.math.IExpressionVariable.PrimaryAttributeVariable;
import de.pnp.manager.component.math.IllegalFormulaException;
import de.pnp.manager.server.database.attributes.PrimaryAttributeRepository;
import de.pnp.manager.server.database.attributes.SecondaryAttributeRepository;
import de.pnp.manager.validation.IsValidExpressionValidator;
import org.bson.types.ObjectId;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A controller to convert {@link SecondaryAttributeDTO} and {@link SecondaryAttribute}.
 */
@Component
public class SecondaryAttributeDTOController {


    private final PrimaryAttributeRepository primaryAttributeRepository;

    private final SecondaryAttributeRepository secondaryAttributeRepository;

    public SecondaryAttributeDTOController(
            @Autowired PrimaryAttributeRepository primaryAttributeRepository,
            @Autowired SecondaryAttributeRepository secondaryAttributeRepository) {
        this.primaryAttributeRepository = primaryAttributeRepository;
        this.secondaryAttributeRepository = secondaryAttributeRepository;
    }

    /**
     * Returns all {@link SecondaryAttributeDTO} of the given universe
     */
    public Collection<SecondaryAttributeDTO> getAll(ObjectId universe) {
        return getAll(universe, null);
    }

    /**
     * Returns all {@link SecondaryAttributeDTO} of the given universe matching the given ids
     */
    public Collection<SecondaryAttributeDTO> getAll(ObjectId universe, @Nullable List<ObjectId> ids) {
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
    public Collection<SecondaryAttributeDTO> insertAll(ObjectId universe,
                                                       List<SecondaryAttributeDTO> attributes) {
        return secondaryAttributeRepository.insertAll(universe, convert(universe, attributes)).stream()
                .map(SecondaryAttributeDTO::from).toList();
    }

    /**
     * Inserts the given {@link SecondaryAttributeDTO} into the universe.
     */
    public SecondaryAttributeDTO insert(ObjectId universe, SecondaryAttributeDTO attribute) {
        return insertAll(universe, List.of(attribute)).stream().findFirst().orElseThrow();
    }

    /**
     * Updates the given {@link SecondaryAttributeDTO}
     */
    public SecondaryAttributeDTO update(ObjectId universe, SecondaryAttributeDTO attribute) {
        Set<IExpressionVariable> attributeVariables = getPrimaryAttributeVariables(universe);

        SecondaryAttribute secondaryAttribute = new SecondaryAttribute(attribute.id(), attribute.name(), attribute.shortName(),
                attribute.consumable(), createExpression(attribute.calculationFormula(), attributeVariables));

        return SecondaryAttributeDTO.from(secondaryAttributeRepository.update(universe, secondaryAttribute));
    }

    /**
     * Returns all supported variables of the given universe.
     */
    public List<String> getSupportedVariables(ObjectId universe) {
        return Stream.concat(getPrimaryAttributeVariables(universe).stream().map(IExpressionVariable::getIdentifier),
                IsValidExpressionValidator.ALLOWED_SECONDARY_ATTRIBUTE_STRING_VARIABLES.stream()).toList();
    }

    /**
     * Converts the {@link SecondaryAttributeDTO} to {@link SecondaryAttribute}.
     */
    public List<SecondaryAttribute> convert(ObjectId universe, List<SecondaryAttributeDTO> attributeDTOS) {
        Set<IExpressionVariable> attributeVariables = getPrimaryAttributeVariables(universe);
        return attributeDTOS.stream().map(
                dto -> new SecondaryAttribute(dto.id(), dto.name(), dto.shortName(), dto.consumable(),
                        createExpression(dto.calculationFormula(), attributeVariables))).toList();
    }

    /**
     * Converts the {@link SecondaryAttributeDTO} to {@link SecondaryAttribute} without any soundness checks.
     * <p>
     * Contains {@link null} for DTOs which can not be converted.
     */
    public List<@Nullable SecondaryAttribute> convertRaw(ObjectId universe, List<SecondaryAttributeDTO> attributeDTOS) {
        Set<IExpressionVariable> attributeVariables = getPrimaryAttributeVariables(universe);

        return attributeDTOS.stream().map(
                dto -> {
                    try {
                        return new SecondaryAttribute(dto.id(), dto.name(), dto.shortName(), dto.consumable(),
                                BinaryExpressionTree.from(dto.calculationFormula(), attributeVariables));
                    } catch (IllegalFormulaException e) {
                        return null;
                    }
                }).toList();
    }

    private Set<IExpressionVariable> getPrimaryAttributeVariables(ObjectId universe) {
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
