package de.pnp.manager.component.math;

import de.pnp.manager.component.attributes.PrimaryAttribute;
import de.pnp.manager.component.math.IExpressionVariable.PrimaryAttributeVariable;
import de.pnp.manager.component.math.IExpressionVariable.StringVariable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link BinaryExpressionTree}.
 */
class BinaryExpressionTreeTest {

    @ParameterizedTest
    @CsvSource({
            "7 +4/2, 9",
            "10/4 +12, 14.5",
            "1+ 2+2, 5",
            "5*-2, -10",
            "-(10 + 2 ), -12",
            "7++ +1, 8",
            "-9 --2, -7",
            "(5 + 9) * 10, 140",
            "1.5 + 2 + 0.5, 4",
            "20 * 1.5, 30",
            "5 + ( 3- 2)*2, 7",
            "abs(-7), 7",
            "'min(1, 2)', 1",
            "'max(1, 2)', 2",
            "'round(1.8)', 2",
            "'round(1.2)', 1",
            "'ceil(1.2)', 2",
            "'floor(1.7)', 1",
            "'max(1, max(3,4))', 4",
            "'floor(max(2.6, 4.1))', 4",
            "'0.5* 2 - round(  0.1* 2 + max(10, 11))', -10"
    })
    void testSimpleExpressions(String formula, double result) throws IllegalFormulaException {
        assertThat(BinaryExpressionTree.from(formula, Set.of()).calculate(Map.of())).isEqualTo(result);
    }

    @Test
    void testVariables() throws IllegalFormulaException {
        StringVariable a = new StringVariable("A");
        StringVariable b = new StringVariable("B");
        Map<IExpressionVariable, Double> variables = Map.of(a, 10d, b, 0.5);
        BinaryExpressionTree expression = BinaryExpressionTree.from("(A + 8) * B", Set.of());

        assertThat(expression.getVariables()).containsExactly(a, b);
        assertThat(expression.calculate(variables)).isEqualTo(9);
    }

    @Test
    void testVariableTransformation() throws IllegalFormulaException {
        PrimaryAttributeVariable strength = new PrimaryAttributeVariable(new PrimaryAttribute(null, "Strength", "ST"));
        PrimaryAttributeVariable charisma = new PrimaryAttributeVariable(new PrimaryAttribute(null, "Charisma", "CH"));
        StringVariable lvl = new StringVariable("LVL");
        Map<IExpressionVariable, Double> variables = Map.of(strength, 8d, charisma, 4d, lvl, 9d);
        BinaryExpressionTree expression = BinaryExpressionTree.from("4 + 0.4 * ST - 0.1 * CH + max(10 * LVL, 2)",
                Set.of(strength, charisma));

        assertThat(expression.getVariables()).containsExactly(strength, charisma, lvl);
        assertThat(expression.calculate(variables)).isEqualTo(96.8);
    }

    @ParameterizedTest
    @CsvSource({
            "(10+3-1)*11, (10 + 3 - 1) * 11",
            "10/4 +12, 10 / 4 + 12",
            "1+ 2+2, 1 + 2 + 2",
            "5*-2, 5 * -2",
            "-(10 + 2 ), -(10 + 2)",
            "7.784++ +1, 7.78 + 1",
            "5 + ( 3- 2)*2, 5 + (3 - 2) * 2",
            "0.5* STR -  0.1* INT + 10, 0.5 * STR - 0.1 * INT + 10",
            "'0.5* STR - round(  0.1* INT + max(10, 11))'  , '0.5 * STR - round(0.1 * INT + max(10, 11))'"
    })
    void testHumanReadableFormat(String formula, String expected) throws IllegalFormulaException {
        assertThat(BinaryExpressionTree.from(formula, Set.of()).toHumanReadableString()).isEqualTo(expected);
    }
}