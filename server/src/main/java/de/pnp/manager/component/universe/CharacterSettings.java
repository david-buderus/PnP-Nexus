package de.pnp.manager.component.universe;

import com.fasterxml.jackson.annotation.JsonCreator;
import de.pnp.manager.component.character.PnPCharacter;
import de.pnp.manager.component.math.BinaryExpressionTree;
import de.pnp.manager.component.math.IllegalFormulaException;

import java.util.Objects;
import java.util.Set;

/**
 * The settings related to {@link PnPCharacter}
 */
public final class CharacterSettings extends SettingsBase {

    /**
     * The default settings
     */
    public static final CharacterSettings DEFAULT = new CharacterSettings(
            2, 12, 50, createFormula("ceil(LVL/5)"), createFormula("50"));

    private final int minPrimaryAttributeValue;

    private final int maxPrimaryAttributeValue;

    private final int maxPrimaryAttributeSum;

    private final BinaryExpressionTree tierFormula;

    private final BinaryExpressionTree talentPointFormula;

    @JsonCreator
    public CharacterSettings(int minPrimaryAttributeValue, int maxPrimaryAttributeValue, int maxPrimaryAttributeSum, BinaryExpressionTree tierFormula, BinaryExpressionTree talentPointFormula) {
        this.minPrimaryAttributeValue = minPrimaryAttributeValue;
        this.maxPrimaryAttributeValue = maxPrimaryAttributeValue;
        this.maxPrimaryAttributeSum = maxPrimaryAttributeSum;
        this.tierFormula = tierFormula;
        this.talentPointFormula = talentPointFormula;
    }

    public int getMinPrimaryAttributeValue() {
        return minPrimaryAttributeValue;
    }

    public int getMaxPrimaryAttributeValue() {
        return maxPrimaryAttributeValue;
    }

    public int getMaxPrimaryAttributeSum() {
        return maxPrimaryAttributeSum;
    }

    public BinaryExpressionTree getTierFormula() {
        return tierFormula;
    }

    public BinaryExpressionTree getTalentPointFormula() {
        return talentPointFormula;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CharacterSettings that = (CharacterSettings) o;
        return minPrimaryAttributeValue == that.minPrimaryAttributeValue
                && maxPrimaryAttributeValue == that.maxPrimaryAttributeValue
                && maxPrimaryAttributeSum == that.maxPrimaryAttributeSum
                && Objects.equals(tierFormula, that.tierFormula)
                && Objects.equals(talentPointFormula, that.talentPointFormula);
    }

    @Override
    public int hashCode() {
        return Objects.hash(minPrimaryAttributeValue, maxPrimaryAttributeValue, maxPrimaryAttributeSum, tierFormula, talentPointFormula);
    }

    private static BinaryExpressionTree createFormula(String formula) {
        try {
            return BinaryExpressionTree.from(formula, Set.of());
        } catch (IllegalFormulaException e) {
            throw new IllegalStateException(e);
        }
    }
}
