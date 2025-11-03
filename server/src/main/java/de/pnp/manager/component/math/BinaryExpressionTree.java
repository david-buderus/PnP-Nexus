package de.pnp.manager.component.math;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.pnp.manager.component.math.IExpressionVariable.StringVariable;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * A binary tree which represents a formula.
 */
public class BinaryExpressionTree {

    @NotNull
    @JsonProperty
    private final IBinaryExpressionTreeNode root;

    @JsonCreator
    private BinaryExpressionTree(IBinaryExpressionTreeNode root) {
        this.root = root;
    }

    /**
     * Calculates the result of the tree with the given constants.
     */
    public double calculate(Map<IExpressionVariable, Double> constants) {
        if (root == null) {
            return Double.NaN;
        }

        return root.calculate(constants);
    }

    /**
     * Returns all variables used in this tree.
     */
    @JsonIgnore
    public Set<IExpressionVariable> getVariables() {
        if (root == null) {
            return Collections.emptySet();
        }

        return root.getVariables();
    }


    /**
     * Returns the underlying formula as a human-readable string.
     */
    public String toHumanReadableString() {
        if (root == null) {
            return "";
        }

        StringBuilder expression = new StringBuilder();
        root.buildHumanReadableString(expression);
        return expression.toString();
    }

    /**
     * Returns if the tree contains any formula.
     */
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BinaryExpressionTree that = (BinaryExpressionTree) o;
        return Objects.equals(root, that.root);
    }

    @Override
    public int hashCode() {
        return Objects.hash(root);
    }

    /**
     * Creates a {@link BinaryExpressionTree} from the given formula and variable transformations.
     */
    public static BinaryExpressionTree from(String formula, Set<IExpressionVariable> knownVariables)
        throws IllegalFormulaException {
        Deque<EOperator> ops = new ArrayDeque<>();
        Deque<IBinaryExpressionTreeNode> stack = new ArrayDeque<>();
        Map<String, IExpressionVariable> variableTransformer = knownVariables.stream()
            .collect(Collectors.toMap(IExpressionVariable::getIdentifier, v -> v));

        boolean needsToBeFollowedByConstant = true;

        int i = 0;
        while (i < formula.length()) {
            char ch = formula.charAt(i);
            EOperator operator = EOperator.from(ch);

            if (operator != null) {
                handleOperator(ops, stack, operator, needsToBeFollowedByConstant);
                i += 1;
                needsToBeFollowedByConstant = operator.needsToBeFollowedByConstant();
            } else if (Character.isSpaceChar(ch)) {
                // Skip space
                i += 1;
            } else {
                String constant = parseConstant(formula, i);
                i += constant.length();

                if (NumberUtils.isCreatable(constant)) {
                    stack.push(new ConstantNode(NumberUtils.createDouble(constant)));
                } else {
                    stack.push(
                        new VariableNode(variableTransformer.getOrDefault(constant, new StringVariable(constant))));
                }
                needsToBeFollowedByConstant = false;
            }
        }

        while (stack.size() > 1) {
            combine(ops, stack);
        }
        while (!ops.isEmpty()) {
            // The formula started with at least one '-'
            combine(ops, stack);
        }

        return new BinaryExpressionTree(stack.peek());
    }

    private static String parseConstant(String formula, int pos) {
        StringBuilder constant = new StringBuilder();

        for (int i = pos; i < formula.length(); i++) {
            char ch = formula.charAt(i);
            if (EOperator.from(ch) != null || Character.isSpaceChar(ch)) {
                return constant.toString();
            }
            constant.append(ch);
        }

        return constant.toString();
    }

    private static void handleOperator(Deque<EOperator> ops, Deque<IBinaryExpressionTreeNode> stack,
        EOperator operator, boolean needsToBeFollowedByConstant) throws IllegalFormulaException {
        if (needsToBeFollowedByConstant) {
            if (operator == EOperator.SUB) {
                operator = EOperator.NEGATE;
            } else if (operator == EOperator.ADD) {
                return;
            } else if (operator != EOperator.OPENING_BRACKET) {
                throw new IllegalFormulaException();
            }
        }
        switch (operator) {
            case OPENING_BRACKET -> ops.push(operator);
            case CLOSING_BRACKET -> {
                while (ops.peek() != EOperator.OPENING_BRACKET) {
                    combine(ops, stack);
                }
                // pop OPENING_BRACKET
                ops.pop();
            }
            default -> {
                while (!ops.isEmpty() && ops.peek().getPrio() >= operator.getPrio()) {
                    combine(ops, stack);
                }

                ops.push(operator);
            }
        }
    }

    private static void combine(Deque<EOperator> ops, Deque<IBinaryExpressionTreeNode> stack)
        throws IllegalFormulaException {
        if (ops.isEmpty()) {
            throw new IllegalFormulaException();
        }
        EOperator operator = ops.pop();

        if (operator == EOperator.NEGATE) {
            if (stack.isEmpty()) {
                throw new IllegalFormulaException();
            }
            stack.push(new NegateInnerNode(stack.pop()));
        } else {
            if (stack.size() < 2) {
                throw new IllegalFormulaException();
            }
            // right first, then left
            IBinaryExpressionTreeNode right = stack.pop();
            IBinaryExpressionTreeNode left = stack.pop();
            stack.push(new TwoParameterInnerNode(operator, left, right));
        }
    }
}
