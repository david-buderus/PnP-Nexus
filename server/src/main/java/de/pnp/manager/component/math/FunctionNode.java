package de.pnp.manager.component.math;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A node which represents a function with its parameters.
 */
class FunctionNode implements IBinaryExpressionTreeNode {

    @JsonProperty
    private final EFunction function;

    @JsonProperty
    private final List<IBinaryExpressionTreeNode> parameters;

    @JsonCreator
    public FunctionNode(EFunction function, List<IBinaryExpressionTreeNode> parameters) {
        this.function = function;
        this.parameters = parameters;
    }

    @Override
    public double calculate(Map<IExpressionVariable, Double> constants) {
        return switch (function) {
            case ABS -> Math.abs(parameters.getFirst().calculate(constants));
            case CEIL -> Math.ceil(parameters.getFirst().calculate(constants));
            case FLOOR -> Math.floor(parameters.getFirst().calculate(constants));
            case ROUND -> Math.round(parameters.getFirst().calculate(constants));
            case MAX -> Math.max(
                    parameters.getFirst().calculate(constants),
                    parameters.get(1).calculate(constants)
            );
            case MIN -> Math.min(
                    parameters.getFirst().calculate(constants),
                    parameters.get(1).calculate(constants)
            );
        };
    }

    @Override
    public Set<IExpressionVariable> getVariables() {
        return parameters.stream().flatMap(p -> p.getVariables().stream()).collect(Collectors.toSet());
    }

    @Override
    public void buildHumanReadableString(StringBuilder builder) {
        builder.append(function.getHumanReadableFormat()).append('(');
        for (int i = 0; i < parameters.size(); i++) {
            parameters.get(i).buildHumanReadableString(builder);
            if (i < parameters.size() - 1) {
                builder.append(", ");
            }
        }
        builder.append(')');
    }
}
