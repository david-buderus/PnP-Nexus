package de.pnp.manager.component.universe;

import de.pnp.manager.component.character.PnPCharacter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * The settings related to the equipment of a {@link PnPCharacter}
 */
public class EquipmentSettings extends SettingsBase {

    /**
     * The default settings
     */
    public static final EquipmentSettings DEFAULT = new EquipmentSettings(2, List.of());


    @PositiveOrZero
    private final int numberOfHandheld;

    @NotNull
    private final List<@Valid JewelleryDefinition> jewelleryDefinitions;

    public EquipmentSettings(int numberOfHandheld, List<@Valid JewelleryDefinition> jewelleryDefinitions) {
        this.numberOfHandheld = numberOfHandheld;
        this.jewelleryDefinitions = jewelleryDefinitions;
    }

    public int getNumberOfHandheld() {
        return numberOfHandheld;
    }

    public List<JewelleryDefinition> getJewelleryDefinitions() {
        return jewelleryDefinitions;
    }

    /**
     * In which jewellery slot what kind of jewellery is allowed and how many.
     */
    public record JewelleryDefinition(@NotBlank String name, @NotNull String tag, @Positive int amount) {

    }

}
