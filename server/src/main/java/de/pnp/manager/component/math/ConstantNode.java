package de.pnp.manager.component.math;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * A node which represents a single constant.
 */
public class ConstantNode implements IBinaryExpressionTreeNode {

    private static final DecimalFormat FORMAT = new DecimalFormat("0.##",
        DecimalFormatSymbols.getInstance(Locale.ENGLISH));

    @JsonProperty
    private final double constant;

    @JsonCreator
    public ConstantNode(double constant) {
        this.constant = constant;
    }

    @Override
    public double calculate(Map<IExpressionVariable, Double> constants) {
        return constant;
    }

    @Override
    public Set<IExpressionVariable> getVariables() {
        return Collections.emptySet();
    }

    @Override
    public void buildHumanReadableString(StringBuilder builder) {
        builder.append(FORMAT.format(constant));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConstantNode that = (ConstantNode) o;
        return Double.compare(that.constant, constant) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(constant);
    }
}
