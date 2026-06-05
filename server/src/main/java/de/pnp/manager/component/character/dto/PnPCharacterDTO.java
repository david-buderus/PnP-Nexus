package de.pnp.manager.component.character.dto;

import de.pnp.manager.component.IDTOWithId;
import de.pnp.manager.component.character.*;
import de.pnp.manager.component.character.traits.ICharacterTrait;
import de.pnp.manager.component.spell.Spell;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;

/**
 * DTO for {@link PnPCharacter}
 */
public record PnPCharacterDTO(
        ObjectId id,
        @Valid @NotNull CharacterDescription description,
        @Valid @NotNull CharacterLevel level,
        @Valid @NotNull CharacterOrigin origin,
        @NotNull List<@Valid ICharacterTrait> advantageTraits,
        @NotNull List<@Valid ICharacterTrait> disadvantageTraits,
        @NotNull @Valid CharacterStatsDto stats,
        @NotNull Map<ObjectId, @NotNull @Valid TalentRollDto> talents,
        @NotNull CharacterEquipment equipment,
        @NotNull CharacterInventory inventory,
        @NotNull List<Spell> spells,
        @NotNull Map<String, String> customFields
) implements IDTOWithId {
    /**
     * DTO for the stats of a talent roll
     */
    public record TalentRollDto(int rawValue, int totalValue) {

    }
}
