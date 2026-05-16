package de.pnp.manager.server.service.character;

import de.pnp.manager.component.character.PnPCharacter;
import de.pnp.manager.component.character.dto.CharacterStatsDto;
import de.pnp.manager.component.character.dto.PnPCharacterDTO;
import de.pnp.manager.security.UniverseOwner;
import de.pnp.manager.security.UniverseRead;
import de.pnp.manager.security.UniverseWrite;
import de.pnp.manager.server.contoller.PnPCharacterDTOConverter;
import de.pnp.manager.server.database.character.PnPCharacterRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Rest service to control characters
 */
@RestController
@Validated
@RequestMapping("api/{universe}/characters")
public class PnPCharacterService {

    private final PnPCharacterDTOConverter converter;

    private final PnPCharacterRepository repository;


    public PnPCharacterService(@Autowired PnPCharacterDTOConverter converter, @Autowired PnPCharacterRepository repository) {
        this.converter = converter;
        this.repository = repository;
    }

    @GetMapping
    @UniverseOwner
    @Operation(summary = "Get all characters from the database", operationId = "getAllCharacters")
    public Collection<PnPCharacterDTO> getAllCharacters(@PathVariable ObjectId universe) {
        Collection<PnPCharacter> all = repository.getAll(universe);
        return converter.convert(universe, all);
    }

    @PostMapping
    @UniverseWrite
    @Operation(summary = "Inserts the objects into the database", operationId = "insertAllCharacters")
    public Collection<PnPCharacterDTO> insertAll(@PathVariable ObjectId universe, @RequestBody List<@Valid PnPCharacterDTO> objects) {
        List<PnPCharacter> toInsert = converter.convertFromDto(universe, objects);
        return converter.convert(universe, repository.insertAll(universe, toInsert));
    }

    @DeleteMapping
    @UniverseWrite
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletes all objects with the given ids from the database", operationId = "deleteAllCharacters")
    public void deleteAll(@PathVariable ObjectId universe, @RequestParam List<ObjectId> ids) {
        if (!repository.removeAll(universe, ids)) {
            throw createNotFound("Unable to find all resource with the given ids");
        }
    }

    @GetMapping("{id}")
    @UniverseRead
    @Operation(summary = "Get an object from the database", operationId = "getCharacter")
    public PnPCharacterDTO get(@PathVariable ObjectId universe, @PathVariable ObjectId id) {
        PnPCharacter character = repository.get(universe, id)
                .orElseThrow(() -> createNotFound("Unable to find resource with id '%s'", id));
        return converter.convert(universe, character);
    }

    @PutMapping("{id}")
    @UniverseWrite
    @Operation(summary = "Updates an object in the database", operationId = "updateCharacter")
    public PnPCharacterDTO update(@PathVariable ObjectId universe, @PathVariable ObjectId id, @RequestBody @Valid PnPCharacterDTO object) {
        if (object.id() != null && !Objects.equals(id, object.id())) {
            throw new ResponseStatusException(BAD_REQUEST, "The id of the object does not match.");
        }
        return converter.convert(universe, repository.update(universe, id, converter.convert(universe, object)));
    }

    @DeleteMapping("{id}")
    @UniverseWrite
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletes an object from the database", operationId = "deleteCharacter")
    public void delete(@PathVariable ObjectId universe, @PathVariable ObjectId id) {
        if (!repository.remove(universe, id)) {
            throw createNotFound("Unable to find resource with id '%s'", id);
        }
    }

    @PostMapping("recalculate")
    @UniverseRead
    @Operation(summary = "Recalculates all entries of the character", operationId = "recalculateEntries")
    public RecalculateEntries recalculateEntries(@PathVariable ObjectId universe, @RequestBody PnPCharacterDTO character) {
        PnPCharacterDTO dto = converter.recalculateEntries(universe, character);
        return new RecalculateEntries(dto.stats().primaryStats(), dto.stats().secondaryStats(), dto.talents());
    }

    /**
     * Recalculated entries in a {@link CharacterStatsDto}
     */
    public record RecalculateEntries(
            Map<ObjectId, CharacterStatsDto.StatsDto> primaryStats,
            Map<ObjectId, CharacterStatsDto.StatsDto> secondaryStats,
            Map<ObjectId, PnPCharacterDTO.TalentRollDto> talents
    ) {
    }

    /**
     * Returns a {@link ResponseStatusException} with a 404 status.
     */
    private ResponseStatusException createNotFound(String text, Object... objects) {
        return new ResponseStatusException(NOT_FOUND, String.format(text, objects));
    }
}
