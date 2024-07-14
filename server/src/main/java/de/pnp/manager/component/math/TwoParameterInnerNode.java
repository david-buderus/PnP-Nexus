package de.pnp.manager.component.math;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Sets;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * A node which represents an operation with two parameters.
 */
class TwoParameterInnerNode implements IBinaryExpressionTreeNode {

    @JsonProperty
    private final EOperator operator;

    @JsonProperty
    private final IBinaryExpressionTreeNode left;

    @JsonProperty
    private final IBinaryExpressionTreeNode right;

    @JsonCreator
    public TwoParameterInnerNode(EOperator operator, IBinaryExpressionTreeNode left,
        IBinaryExpressionTreeNode right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    public double calculate(Map<IExpressionVariable, Double> constants) {
        double leftValue = left.calculate(constants);
        double rightValue = right.calculate(constants);
        return switch (operator) {
            case ADD -> leftValue + rightValue;
            case SUB -> leftValue - rightValue;
            case MULTIPLY -> leftValue * rightValue;
            case DIVIDE -> leftValue / rightValue;
            default -> throw new IllegalStateException("The operator can not be used here: " + operator);
        };
    }

    @Override
    public Set<IExpressionVariable> getVariables() {
        return Sets.union(left.getVariables(), right.getVariables());
    }

    @Override
    public void buildHumanReadableString(StringBuilder builder) {
        buildChildString(builder, left);
        builder.append(" ").append(operator.getHumanReadableFormat()).append(" ");
        buildChildString(builder, right);
    }

    private void buildChildString(StringBuilder builder, IBinaryExpressionTreeNode child) {
        if (child instanceof TwoParameterInnerNode node && node.operator.getPrio() < operator.getPrio()) {
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
        TwoParameterInnerNode that = (TwoParameterInnerNode) o;
        return operator == that.operator && Objects.equals(left, that.left) && Objects.equals(right,
            that.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operator, left, right);
    }
}
