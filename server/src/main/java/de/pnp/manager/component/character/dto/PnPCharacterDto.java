package de.pnp.manager.component.character.dto;

import de.pnp.manager.component.character.*;
import de.pnp.manager.component.character.traits.ICharacterTrait;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;
import java.util.Map;

/**
 * DTO for {@link PnPCharacter}
 */
public record PnPCharacterDto(
        ObjectId id,
        @Valid @NotNull CharacterDescription description,
        @Valid @NotNull CharacterLevel level,
        @DBRef @NotNull Species species,
        @DBRef Nation nation,
        @NotNull List<@Valid ICharacterTrait> advantageTraits,
        @NotNull List<@Valid ICharacterTrait> disadvantageTraits,
        @NotNull @Valid CharacterStatsDto stats,
        @NotNull Map<ObjectId, @NotNull @Valid TalentRollDto> talents,
        @NotNull CharacterEquipment equipment,
        @NotNull CharacterInventory inventory
) {
    /**
     * DTO for the stats of a talent roll
     */
    public record TalentRollDto(int rawValue, int totalValue) {

    }
}
