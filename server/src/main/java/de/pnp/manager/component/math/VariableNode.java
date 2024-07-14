package de.pnp.manager.component.math;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * A node which represents a {@link IExpressionVariable variable}.
 */
class VariableNode implements IBinaryExpressionTreeNode {

    @JsonProperty
    private final IExpressionVariable variable;

    @JsonCreator
    public VariableNode(IExpressionVariable variable) {
        this.variable = variable;
    }

    @Override
    public double calculate(Map<IExpressionVariable, Double> constants) {
        return constants.getOrDefault(variable, 0d);
    }

    @Override
    public Set<IExpressionVariable> getVariables() {
        return Set.of(variable);
    }

    @Override
    public void buildHumanReadableString(StringBuilder builder) {
        builder.append(variable.getIdentifier());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VariableNode that = (VariableNode) o;
        return Objects.equals(variable, that.variable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variable);
    }
}
