package de.pnp.manager.component.universe;

import com.fasterxml.jackson.annotation.JsonCreator;
import de.pnp.manager.component.character.PnPCharacter;
import de.pnp.manager.component.math.BinaryExpressionTree;
import de.pnp.manager.component.math.IllegalFormulaException;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

import java.util.List;
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
            2, 12, 50, createFormula("ceil(LVL/5)"), createFormula("50"), List.of());

    private final int minPrimaryAttributeValue;

    private final int maxPrimaryAttributeValue;

    private final int maxPrimaryAttributeSum;

    private final BinaryExpressionTree tierFormula;

    private final BinaryExpressionTree talentPointFormula;

    private final List<InventorySizeEntry> inventorySizes;

    @JsonCreator
    public CharacterSettings(int minPrimaryAttributeValue, int maxPrimaryAttributeValue, int maxPrimaryAttributeSum,
                             BinaryExpressionTree tierFormula, BinaryExpressionTree talentPointFormula,
                             List<InventorySizeEntry> inventorySizes) {
        this.minPrimaryAttributeValue = minPrimaryAttributeValue;
        this.maxPrimaryAttributeValue = maxPrimaryAttributeValue;
        this.maxPrimaryAttributeSum = maxPrimaryAttributeSum;
        this.tierFormula = tierFormula;
        this.talentPointFormula = talentPointFormula;
        this.inventorySizes = inventorySizes;
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

    public List<InventorySizeEntry> getInventorySizes() {
        return inventorySizes;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CharacterSettings settings = (CharacterSettings) o;
        return minPrimaryAttributeValue == settings.minPrimaryAttributeValue
                && maxPrimaryAttributeValue == settings.maxPrimaryAttributeValue
                && maxPrimaryAttributeSum == settings.maxPrimaryAttributeSum
                && Objects.equals(tierFormula, settings.tierFormula)
                && Objects.equals(talentPointFormula, settings.talentPointFormula)
                && Objects.equals(inventorySizes, settings.inventorySizes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(minPrimaryAttributeValue, maxPrimaryAttributeValue, maxPrimaryAttributeSum, tierFormula, talentPointFormula, inventorySizes);
    }

    private static BinaryExpressionTree createFormula(String formula) {
        try {
            return BinaryExpressionTree.from(formula, Set.of());
        } catch (IllegalFormulaException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Entry for inventory sizes
     */
    public record InventorySizeEntry(@NotEmpty String name, @Positive int size) {
    }
}
