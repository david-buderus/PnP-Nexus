package de.pnp.manager.component.universe;

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
    public static final CharacterSettings DEFAULT = new CharacterSettings(2, List.of(), List.of());
    private final @PositiveOrZero int numberOfHandheld;
    private final @NotNull List<ArmorDefinition> armorDefinitions;
    private final @NotNull List<ArmorDefinition> jewelleryDefinitions;

    public CharacterSettings(
        @PositiveOrZero int numberOfHandheld,
        @NotNull List<ArmorDefinition> armorDefinitions,
        @NotNull List<ArmorDefinition> jewelleryDefinitions
    ) {
        this.numberOfHandheld = numberOfHandheld;
        this.armorDefinitions = armorDefinitions;
        this.jewelleryDefinitions = jewelleryDefinitions;
    }

    public int getNumberOfHandheld() {
        return numberOfHandheld;
    }

    public List<ArmorDefinition> getArmorDefinitions() {
        return armorDefinitions;
    }

    public List<ArmorDefinition> getJewelleryDefinitions() {
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
