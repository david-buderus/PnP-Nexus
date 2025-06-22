package de.pnp.manager.server.service.character;

import de.pnp.manager.component.Dice;
import de.pnp.manager.component.attributes.PrimaryAttribute;
import de.pnp.manager.component.attributes.SecondaryAttribute;
import de.pnp.manager.component.character.*;
import de.pnp.manager.component.character.dto.CharacterStatsDto;
import de.pnp.manager.component.character.dto.PnPCharacterDto;
import de.pnp.manager.component.character.stats.Stat;
import de.pnp.manager.component.inventory.Inventory;
import de.pnp.manager.component.inventory.ItemStack;
import de.pnp.manager.component.inventory.equipment.ShieldEquipment;
import de.pnp.manager.component.inventory.equipment.WeaponEquipment;
import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.equipable.Shield;
import de.pnp.manager.component.item.equipable.Weapon;
import de.pnp.manager.security.UniverseOwner;
import de.pnp.manager.server.database.TalentRepository;
import de.pnp.manager.server.database.attributes.PrimaryAttributeRepository;
import de.pnp.manager.server.database.attributes.SecondaryAttributeRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/{universe}/characters")
public class PnPCharacterService {

    @Autowired
    private PrimaryAttributeRepository primaryAttributeRepository;

    @Autowired
    private SecondaryAttributeRepository secondaryAttributeRepository;

    @Autowired
    private TalentRepository talentRepository;

    @GetMapping
    @UniverseOwner
    @Operation(summary = "Get all characters from the database", operationId = "getAllCharacters")
    public Collection<PnPCharacterDto> getAllCharacters(@PathVariable String universe) {
        Collection<PrimaryAttribute> primaryAttributes = primaryAttributeRepository.getAll(universe);
        Collection<SecondaryAttribute> secondaryAttributes = secondaryAttributeRepository.getAll(universe);

        CharacterStats stats = new CharacterStats(
                primaryAttributes.stream()
                        .collect(Collectors.toMap(Function.identity(), a -> new Stat(5))),
                secondaryAttributes.stream()
                        .collect(Collectors.toMap(Function.identity(), a -> new Stat(2)))
        );
        stats.recalculateSecondaryStats(primaryAttributes, secondaryAttributes);

        return transform(universe, List.of(
                new PnPCharacter(
                        null,
                        new CharacterDescription("Name", 20, "Profession", "Male", "Backstroy"),
                        new CharacterLevel(2, 1, 0),
                        new Species(null, "Race", "Race description", true, List.of(), List.of(), List.of()),
                        null,
                        List.of(),
                        List.of(),
                        stats,
                        new CharacterTalents(Map.of()),
                        new CharacterEquipment(List.of(
                                new WeaponEquipment(1, new Weapon(null, "Weapon", Set.of(), "", "", ERarity.COMMON, 100, 1, "", "", null, 2, 1, 0, 1, Dice.simpleDice(6), 1, 1), 0)
                        ), new ShieldEquipment(1, new Shield(null, "Shield", Set.of(), "", "", ERarity.COMMON, 100, 1, "", "", null, 2, 1, 0, Dice.simpleDice(6), 1, 1, 2, 1, 1), 0), Map.of(), Map.of()),
                        new CharacterInventory(new Inventory(100, List.of(
                                new ItemStack<>(5, new Item(null, "Item", Set.of(), "", "", ERarity.COMMON, 54, 2, "", "", 100, 0))
                        )), 52135),
                        List.of()
                )
        ));
    }

    private List<PnPCharacterDto> transform(String universe, List<PnPCharacter> characters) {
        Collection<PrimaryAttribute> primaryAttributes = primaryAttributeRepository.getAll(universe);
        Collection<SecondaryAttribute> secondaryAttributes = secondaryAttributeRepository.getAll(universe);
        Collection<Talent> talents = talentRepository.getAll(universe);

        List<PnPCharacterDto> dtos = new ArrayList<>();

        for (PnPCharacter character : characters) {
            Map<ObjectId, CharacterStatsDto.StatsDto> primaryStats = new HashMap<>();
            for (PrimaryAttribute attribute : primaryAttributes) {
                primaryStats.put(attribute.getId(), new CharacterStatsDto.StatsDto(
                        character.getStats().get(attribute).getRawValue(),
                        character.getStats().get(attribute).getFlatModifier(),
                        character.getPrimaryStat(attribute)
                ));
            }

            Map<ObjectId, CharacterStatsDto.StatsDto> secondaryStats = new HashMap<>();
            for (SecondaryAttribute attribute : secondaryAttributes) {
                secondaryStats.put(attribute.getId(), new CharacterStatsDto.StatsDto(
                        character.getStats().get(attribute).getRawValue(),
                        character.getStats().get(attribute).getFlatModifier(),
                        character.getSecondaryStat(attribute)
                ));
            }

            Map<ObjectId, PnPCharacterDto.TalentRollDto> talentDto = new HashMap<>();
            for (Talent talent : talents) {
                talentDto.put(talent.getId(), new PnPCharacterDto.TalentRollDto(
                        character.getTalents().getRoll(talent),
                        character.getTalentRoll(talent)
                ));
            }

            dtos.add(new PnPCharacterDto(
                    character.getId(),
                    character.getDescription(),
                    character.getLevel(),
                    character.getSpecies(),
                    character.getNation(),
                    character.getAdvantageTraits(),
                    character.getDisadvantageTraits(),
                    new CharacterStatsDto(primaryStats, secondaryStats),
                    talentDto,
                    character.getEquipment(),
                    character.getInventory()
            ));
        }

        return dtos;
    }
}
