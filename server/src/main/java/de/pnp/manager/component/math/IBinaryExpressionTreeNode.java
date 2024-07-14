package de.pnp.manager.component.math;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.Map;
import java.util.Set;

/**
 * A node in a {@link BinaryExpressionTree}.
 */
@JsonSubTypes({
    @JsonSubTypes.Type(value = ConstantNode.class, name = "ConstantNode"),
    @JsonSubTypes.Type(value = NegateInnerNode.class, name = "NegateInnerNode"),
    @JsonSubTypes.Type(value = TwoParameterInnerNode.class, name = "TwoParameterInnerNode"),
    @JsonSubTypes.Type(value = VariableNode.class, name = "VariableNode"),
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
interface IBinaryExpressionTreeNode {

    /**
     * Calculates the result of the node and its children with the given constants.
     */
    double calculate(Map<IExpressionVariable, Double> constants);

    /**
     * Returns all variables used in this node and its children.
     */
    @JsonIgnore
    Set<IExpressionVariable> getVariables();

    /**
     * Appends a human-readable expression to the builder, matching the node and its children.
     */
    void buildHumanReadableString(StringBuilder builder);
}
