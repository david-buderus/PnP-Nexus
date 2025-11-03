package de.pnp.manager.component.math;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * A node which represents a negation.
 */
class NegateInnerNode implements IBinaryExpressionTreeNode {

    @JsonProperty
    private final IBinaryExpressionTreeNode child;

    @JsonCreator
    public NegateInnerNode(IBinaryExpressionTreeNode child) {
        this.child = child;
    }

    @Override
    public double calculate(Map<IExpressionVariable, Double> constants) {
        return -child.calculate(constants);
    }

    @Override
    public Set<IExpressionVariable> getVariables() {
        return child.getVariables();
    }

    @Override
    public void buildHumanReadableString(StringBuilder builder) {
        builder.append("-");
        if (child instanceof TwoParameterInnerNode) {
            builder.append("(");
            child.buildHumanReadableString(builder);
            builder.append(")");
        } else {
            child.buildHumanReadableString(builder);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NegateInnerNode that = (NegateInnerNode) o;
        return Objects.equals(child, that.child);
    }

    @Override
    public int hashCode() {
        return Objects.hash(child);
    }
}
