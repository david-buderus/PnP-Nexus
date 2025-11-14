package de.pnp.manager.server.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.pnp.manager.component.math.BinaryExpressionTree;
import de.pnp.manager.component.math.IExpressionVariable;
import de.pnp.manager.component.math.IExpressionVariable.PrimaryAttributeVariable;
import de.pnp.manager.component.math.IExpressionVariable.StringVariable;
import de.pnp.manager.component.math.IllegalFormulaException;
import de.pnp.manager.server.database.attributes.PrimaryAttributeRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import static de.pnp.manager.validation.IsValidExpressionValidator.ALLOWED_SECONDARY_ATTRIBUTE_STRING_VARIABLES;

/**
 * Service to create and use {@link BinaryExpressionTree} in the front-end.
 */
@RestController
@Validated
@RequestMapping("/api/expressions")
public class BinaryExpressionTreeService {

    @Autowired
    private PrimaryAttributeRepository primaryAttributeRepository;

    @PostMapping("secondary-attributes/{universe}")
    @Operation(summary = "Creates an expression for secondary attributes", operationId = "createExpressionForSecondaryAttributes")
    public BinaryExpressionTree createExpressionForSecondaryAttributes(@PathVariable ObjectId universe,
                                                                       @RequestBody String formula) {

        BinaryExpressionTree tree;
        try {
            tree = BinaryExpressionTree.from(formula, getPrimaryAttributeVariables(universe));
        } catch (IllegalFormulaException e) {
            throw createResponseException("expression.invalid");
        }

        if (tree.getVariables().stream().filter(StringVariable.class::isInstance)
                .anyMatch(v -> !ALLOWED_SECONDARY_ATTRIBUTE_STRING_VARIABLES.contains(((StringVariable) v).variable()))) {
            throw createResponseException("expression.unknownVariable");
        }

        return tree;
    }

    @PostMapping("result")
    @Operation(summary = "Calculates the results for the given formulas", operationId = "calculateResults")
    public List<Double> calculateResults(@RequestBody CalculationRequest request) {
        Map<IExpressionVariable, Double> variableConstants = request.getVariableConstants();

        return request.formulas.stream().map(formula -> {
            if (StringUtils.isBlank(formula)) {
                return Double.NaN;
            }
            try {
                return BinaryExpressionTree.from(formula, Set.of()).calculate(variableConstants);
            } catch (IllegalFormulaException e) {
                return Double.NaN;
            }
        }).toList();
    }

    @PostMapping("human-readable")
    @Operation(summary = "Returns human readable strings for the given expressions", operationId = "toHumanReadableFormat")
    public List<String> toHumanReadableFormat(@RequestBody List<@Valid BinaryExpressionTree> expressions) {
        return expressions.stream().map(expression -> {
            if (expression != null) {
                return expression.toHumanReadableString();
            } else {
                return "";
            }
        }).toList();
    }

    private Set<IExpressionVariable> getPrimaryAttributeVariables(ObjectId universe) {
        return primaryAttributeRepository.getAll(universe).stream().map(PrimaryAttributeVariable::new)
                .collect(Collectors.toSet());
    }

    private ResponseStatusException createResponseException(String messageKey) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale());
        String errorMessage = bundle.getString(messageKey);
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
    }

    /**
     * A wrapper for a calculation request.
     */
    public record CalculationRequest(List<String> formulas, Map<String, Double> constants) {

        /**
         * Returns {@link #constants} as {@link IExpressionVariable}
         */
        @JsonIgnore
        public Map<IExpressionVariable, Double> getVariableConstants() {
            return constants.entrySet().stream().collect(Collectors.toMap(e -> new StringVariable(e.getKey()),
                    Entry::getValue));
        }
    }
}
