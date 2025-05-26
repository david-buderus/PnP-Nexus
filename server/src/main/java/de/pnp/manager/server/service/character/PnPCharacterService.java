package de.pnp.manager.server.service.character;

import com.google.common.collect.MultimapBuilder;
import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.component.character.*;
import de.pnp.manager.component.character.stats.Stat;
import de.pnp.manager.component.inventory.Inventory;
import de.pnp.manager.security.UniverseOwner;
import de.pnp.manager.server.database.attributes.PrimaryAttributeRepository;
import de.pnp.manager.server.database.attributes.SecondaryAttributeRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/{universe}/characters")
public class PnPCharacterService {

    @Autowired
    private PrimaryAttributeRepository primaryAttributeRepository;

    @Autowired
    private SecondaryAttributeRepository secondaryAttributeRepository;

    @GetMapping
    @UniverseOwner
    @Operation(summary = "Get all characters from the database", operationId = "getAllCharacters")
    public Collection<PnPCharacter> getAllCharacters(@PathVariable String universe) {
        CharacterStats stats = new CharacterStats(
                primaryAttributeRepository.getAll(universe).stream()
                        .collect(Collectors.toMap(DatabaseObject::getId, a -> new Stat(5))),
                secondaryAttributeRepository.getAll(universe).stream()
                        .collect(Collectors.toMap(DatabaseObject::getId, a -> new Stat(2)))
        );

        return List.of(
                new PnPCharacter(
                        null,
                        new CharacterDescription("Name", 20, "Profession", "Male", "Backstroy"),
                        new Species(null, "Race", "Race description", true, List.of(), List.of(), List.of()),
                        null,
                        List.of(),
                        List.of(),
                        stats,
                        new CharacterTalents(Map.of()),
                        new CharacterEquipment(List.of(), Map.of(), MultimapBuilder.hashKeys().arrayListValues().build()),
                        new CharacterInventory(new Inventory(100, List.of()), 52135),
                        List.of(),
                        4,
                        0
                )
        );
    }
}
