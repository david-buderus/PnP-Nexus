package de.pnp.manager.component.universe;

import com.fasterxml.jackson.annotation.JsonCreator;
import de.pnp.manager.component.character.PnPCharacter;
import de.pnp.manager.component.item.ItemType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Objects;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * The settings related to {@link PnPCharacter}
 */
public final class CharacterSettings extends SettingsBase {

    /**
     * The default settings
     */
    public static final CharacterSettings DEFAULT = new CharacterSettings(2, 12, 50, 2, List.of(), List.of());

    @PositiveOrZero
    private final int minPrimaryAttributeValue;

    @Positive
    private final int maxPrimaryAttributeValue;

    @Positive
    private final int maxPrimaryAttributeSum;

    @PositiveOrZero
    private final int numberOfHandheld;

    @NotNull
    private final List<ArmorDefinition> armorDefinitions;

    @NotNull
    private final List<JewelleryDefinition> jewelleryDefinitions;

    @JsonCreator
    public CharacterSettings(int minPrimaryAttributeValue, int maxPrimaryAttributeValue, int maxPrimaryAttributeSum,
        int numberOfHandheld, List<ArmorDefinition> armorDefinitions, List<JewelleryDefinition> jewelleryDefinitions) {
        this.minPrimaryAttributeValue = minPrimaryAttributeValue;
        this.maxPrimaryAttributeValue = maxPrimaryAttributeValue;
        this.maxPrimaryAttributeSum = maxPrimaryAttributeSum;
        this.numberOfHandheld = numberOfHandheld;
        this.armorDefinitions = armorDefinitions;
        this.jewelleryDefinitions = jewelleryDefinitions;
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

    public int getNumberOfHandheld() {
        return numberOfHandheld;
    }

    public List<ArmorDefinition> getArmorDefinitions() {
        return armorDefinitions;
    }

    public List<JewelleryDefinition> getJewelleryDefinitions() {
        return jewelleryDefinitions;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (CharacterSettings) obj;
        return this.numberOfHandheld == that.numberOfHandheld &&
            Objects.equals(this.armorDefinitions, that.armorDefinitions) &&
            Objects.equals(this.jewelleryDefinitions, that.jewelleryDefinitions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfHandheld, armorDefinitions, jewelleryDefinitions);
    }

    /**
     * In which armor slot what kind of armor is allowed.
     */
    public record ArmorDefinition(@NotBlank String name, @DBRef @NotNull ItemType type) {

    }

    /**
     * In which jewellery slot what kind of jewellery is allowed and how many.
     */
    public record JewelleryDefinition(@NotBlank String name, @DBRef @NotNull ItemType type, @Positive int amount) {

    }
}
