package de.pnp.manager.server.service;

import de.pnp.manager.component.math.BinaryExpressionTree;
import de.pnp.manager.component.math.IExpressionVariable;
import de.pnp.manager.component.math.IExpressionVariable.PrimaryAttributeVariable;
import de.pnp.manager.component.math.IExpressionVariable.StringVariable;
import de.pnp.manager.component.math.IllegalFormulaException;
import de.pnp.manager.server.database.attributes.PrimaryAttributeRepository;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Service to create and use {@link BinaryExpressionTree} in the front-end.
 */
@RestController
@Validated
@RequestMapping("/api/{universe}/expressions")
public class BinaryExpressionTreeService {

    private static final Set<String> ALLOWED_STRING_VARIABLES = Set.of("LVL");

    @Autowired
    private PrimaryAttributeRepository primaryAttributeRepository;

    @PostMapping("secondary-attributes")
    @Operation(summary = "Creates an expression for secondary attributes", operationId = "createExpressionForSecondaryAttributes")
    public BinaryExpressionTree createExpressionForSecondaryAttributes(@PathVariable String universe,
        @RequestBody String formula) {

        BinaryExpressionTree tree;
        try {
            tree = BinaryExpressionTree.from(formula, getPrimaryAttributeVariables(universe));
        } catch (IllegalFormulaException e) {
            throw createResponseException("expression.invalid");
        }

        if (tree.getVariables().stream().filter(StringVariable.class::isInstance)
            .anyMatch(v -> !ALLOWED_STRING_VARIABLES.contains(((StringVariable) v).variable()))) {
            throw createResponseException("expression.unknownVariable");
        }

        return tree;
    }

    @PostMapping("human-readable")
    @Operation(summary = "Creates an expression for secondary attributes", operationId = "toHumanReadableFormat")
    public List<String> toHumanReadableFormat(@RequestBody List<BinaryExpressionTree> expressions) {
        return expressions.stream().map(BinaryExpressionTree::asHumanReadableString).toList();
    }

    private Set<IExpressionVariable> getPrimaryAttributeVariables(String universe) {
        return primaryAttributeRepository.getAll(universe).stream().map(PrimaryAttributeVariable::new)
            .collect(Collectors.toSet());
    }

    private ResponseStatusException createResponseException(String messageKey) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale());
        String errorMessage = bundle.getString(messageKey);
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
    }
}
