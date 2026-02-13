package de.pnp.manager.server.service.character;

import de.pnp.manager.component.character.dto.CharacterStatsDto;
import de.pnp.manager.component.character.dto.PnPCharacterDTO;
import de.pnp.manager.security.UniverseOwner;
import de.pnp.manager.security.UniverseRead;
import de.pnp.manager.server.contoller.PnPCharacterDTOConverter;
import io.swagger.v3.oas.annotations.Operation;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Rest service to control characters
 */
@RestController
@RequestMapping("api/{universe}/characters")
public class PnPCharacterService {

    private final PnPCharacterDTOConverter converter;

    public PnPCharacterService(@Autowired PnPCharacterDTOConverter converter) {
        this.converter = converter;
    }

    @GetMapping
    @UniverseOwner
    @Operation(summary = "Get all characters from the database", operationId = "getAllCharacters")
    public Collection<PnPCharacterDTO> getAllCharacters(@PathVariable ObjectId universe) {
        return List.of();
    }

    @PostMapping
    @UniverseRead
    @Operation(summary = "Recalculates all entries of the character", operationId = "recalculateEntries")
    public RecalculateEntries recalculateEntries(@PathVariable ObjectId universe, @RequestBody PnPCharacterDTO character) {
        PnPCharacterDTO dto = converter.recalculateEntries(universe, character);
        return new RecalculateEntries(dto.stats().secondaryStats(), dto.talents());
    }

    /**
     * Recalculated entries in a {@link CharacterStatsDto}
     */
    public record RecalculateEntries(
            Map<ObjectId, CharacterStatsDto.StatsDto> secondaryStats,
            Map<ObjectId, PnPCharacterDTO.TalentRollDto> talents
    ) {
    }
}
