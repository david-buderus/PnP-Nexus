package de.pnp.manager.component.character.dto;

import de.pnp.manager.component.character.CharacterStats;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;

import java.util.Map;

/**
 * Dto for {@link CharacterStats}
 */
public record CharacterStatsDto(
        @NotNull Map<ObjectId, @Valid StatsDto> primaryStats,
        @NotNull Map<ObjectId, @Valid StatsDto> secondaryStats
) {

    /**
     * Dto for stats
     */
    public record StatsDto(
            int rawValue,
            int flatModifier,
            int totalValue
    ) {

    }
}
