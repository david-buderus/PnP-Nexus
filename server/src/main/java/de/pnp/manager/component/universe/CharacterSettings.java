package de.pnp.manager.component.universe;

import com.fasterxml.jackson.annotation.JsonCreator;
import de.pnp.manager.component.character.PnPCharacter;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.Objects;

/**
 * The settings related to {@link PnPCharacter}
 */
public final class CharacterSettings extends SettingsBase {

    /**
     * The default settings
     */
    public static final CharacterSettings DEFAULT = new CharacterSettings(2, 12, 50);

    @PositiveOrZero
    private final int minPrimaryAttributeValue;

    @Positive
    private final int maxPrimaryAttributeValue;

    @Positive
    private final int maxPrimaryAttributeSum;

    @JsonCreator
    public CharacterSettings(int minPrimaryAttributeValue, int maxPrimaryAttributeValue, int maxPrimaryAttributeSum) {
        this.minPrimaryAttributeValue = minPrimaryAttributeValue;
        this.maxPrimaryAttributeValue = maxPrimaryAttributeValue;
        this.maxPrimaryAttributeSum = maxPrimaryAttributeSum;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CharacterSettings that = (CharacterSettings) o;
        return getMinPrimaryAttributeValue() == that.getMinPrimaryAttributeValue()
            && getMaxPrimaryAttributeValue() == that.getMaxPrimaryAttributeValue()
            && getMaxPrimaryAttributeSum() == that.getMaxPrimaryAttributeSum();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMinPrimaryAttributeValue(), getMaxPrimaryAttributeValue(), getMaxPrimaryAttributeSum());
    }
}
