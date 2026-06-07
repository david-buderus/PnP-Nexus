package de.pnp.manager.component.universe;

import com.fasterxml.jackson.annotation.JsonCreator;
import de.pnp.manager.component.character.PnPCharacter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

/**
 * The settings related to the equipment of a {@link PnPCharacter}
 */
public class EquipmentSettings extends SettingsBase {

    /**
     * The default settings
     */
    public static final EquipmentSettings DEFAULT = new EquipmentSettings(List.of());


    @NotNull
    private final List<@Valid JewelleryDefinition> jewelleryDefinitions;

    @JsonCreator
    public EquipmentSettings(List<@Valid JewelleryDefinition> jewelleryDefinitions) {
        this.jewelleryDefinitions = jewelleryDefinitions;
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
