package de.pnp.manager.server.service.character;

import de.pnp.manager.Tag;
import de.pnp.manager.component.Dice;
import de.pnp.manager.component.EAction;
import de.pnp.manager.component.ECalculation;
import de.pnp.manager.component.IResourceUsage;
import de.pnp.manager.component.attributes.PrimaryAttribute;
import de.pnp.manager.component.attributes.SecondaryAttribute;
import de.pnp.manager.component.character.*;
import de.pnp.manager.component.character.dto.CharacterStatsDto;
import de.pnp.manager.component.character.dto.PnPCharacterDto;
import de.pnp.manager.component.character.stats.Stat;
import de.pnp.manager.component.character.traits.SimpleCharacterTrait;
import de.pnp.manager.component.character.traits.StatTrait;
import de.pnp.manager.component.inventory.Inventory;
import de.pnp.manager.component.inventory.ItemStack;
import de.pnp.manager.component.inventory.equipment.ArmorEquipment;
import de.pnp.manager.component.inventory.equipment.Equipment;
import de.pnp.manager.component.inventory.equipment.ShieldEquipment;
import de.pnp.manager.component.inventory.equipment.WeaponEquipment;
import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.item.equipable.*;
import de.pnp.manager.component.spell.ECastingType;
import de.pnp.manager.component.spell.Spell;
import de.pnp.manager.component.universe.EquipmentSettings;
import de.pnp.manager.security.UniverseOwner;
import de.pnp.manager.security.UniverseRead;
import de.pnp.manager.server.database.TalentRepository;
import de.pnp.manager.server.database.attributes.PrimaryAttributeRepository;
import de.pnp.manager.server.database.attributes.SecondaryAttributeRepository;
import de.pnp.manager.server.database.universe.UniverseSettingsRepository;
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

/**
 * Rest service to control characters
 */
@RestController
@RequestMapping("api/{universe}/characters")
public class PnPCharacterService {

    private final PrimaryAttributeRepository primaryAttributeRepository;
    private final SecondaryAttributeRepository secondaryAttributeRepository;
    private final TalentRepository talentRepository;
    private final UniverseSettingsRepository universeSettingsRepository;

    public PnPCharacterService(@Autowired PrimaryAttributeRepository primaryAttributeRepository,
                               @Autowired SecondaryAttributeRepository secondaryAttributeRepository,
                               @Autowired TalentRepository talentRepository,
                               @Autowired UniverseSettingsRepository universeSettingsRepository) {
        this.primaryAttributeRepository = primaryAttributeRepository;
        this.secondaryAttributeRepository = secondaryAttributeRepository;
        this.talentRepository = talentRepository;
        this.universeSettingsRepository = universeSettingsRepository;
    }

    @GetMapping
    @UniverseOwner
    @Operation(summary = "Get all characters from the database", operationId = "getAllCharacters")
    public Collection<PnPCharacterDto> getAllCharacters(@PathVariable String universe) {
        return transform(universe, List.of());
    }

    @GetMapping("example")
    @UniverseRead
    @Operation(summary = "Generates an example character", operationId = "getExampleCharacter")
    public PnPCharacterDto getExampleCharacter(@PathVariable String universe) {
        Collection<PrimaryAttribute> primaryAttributes = primaryAttributeRepository.getAll(universe);
        Collection<SecondaryAttribute> secondaryAttributes = secondaryAttributeRepository.getAll(universe);
        Collection<Talent> talents = talentRepository.getAll(universe);
        EquipmentSettings settings = universeSettingsRepository.getSettings(universe, EquipmentSettings.class);

        CharacterStats stats = new CharacterStats(
                primaryAttributes.stream()
                        .collect(Collectors.toMap(Function.identity(), a -> new Stat(5))),
                secondaryAttributes.stream()
                        .collect(Collectors.toMap(Function.identity(), a -> new Stat(2)))
        );
        stats.recalculateSecondaryStats(primaryAttributes, secondaryAttributes);

        Optional<Talent> talent = talents.stream().findFirst();

        Map<String, List<Equipment<Jewellery>>> jewellery = new HashMap<>();
        if (!settings.getJewelleryDefinitions().isEmpty()) {
            EquipmentSettings.JewelleryDefinition definition = settings.getJewelleryDefinitions().getFirst();
            jewellery.put(definition.name(), List.of(new Equipment<>(1,
                    new Jewellery(null, "Jewellery", Set.of(Tag.from(definition.tag())), "", "", ERarity.COMMON, 200, 1, "", "", null, 1, 1, 1)
            )));
        }

        return transform(new PnPCharacter(
                null,
                new CharacterDescription("Name", 20, "Profession", "Male", "Backstory", "Appearance", "Personality", "Goals", "Deficits", "Affiliations"),
                new CharacterLevel(2, 1, 0),
                new Species(null, "Race", "Race description", true, List.of(), List.of(), List.of()),
                null,
                List.of(
                        new SimpleCharacterTrait("Some Advantage")
                ),
                List.of(
                        new StatTrait.SecondaryStatTrait(ECalculation.MULTIPLICATIVE, 10, secondaryAttributes.stream().findFirst().orElseThrow(), "Some Disadvantage")
                ),
                stats,
                new CharacterTalents(talent.stream().collect(Collectors.toMap(t -> t, t -> 2))),
                new CharacterEquipment(
                        List.of(new WeaponEquipment(1, new Weapon(null, "Weapon", Set.of(), "", "", ERarity.COMMON, 100, 1, "", "", null, 2, 1, 0, 1, Dice.simpleDice(6), 1, 1), 0)),
                        new ShieldEquipment(1, new Shield(null, "Shield", Set.of(), "", "", ERarity.COMMON, 100, 1, "", "", null, 2, 1, 0, Dice.simpleDice(6), 1, 1, 2, 1, 1), 0),
                        Map.of(EArmorSlot.BODY, new ArmorEquipment(1, new Armor(null, "Body", Set.of(), "", "", ERarity.COMMON, 100, 1, "", "", null, 1, EArmorSlot.BODY, 3, 2, 1, 1, 1), 0)),
                        jewellery
                ),
                new CharacterInventory(new Inventory(100, List.of(
                        new ItemStack<>(5, new Item(null, "Item", Set.of(), "", "", ERarity.COMMON, 54, 2, "", "", 100, 0))
                )), 52135),
                List.of(
                        new Spell(null, "Spell", "Effect", List.of(new IResourceUsage.MaterialUsage(1, new Material(null, "Material", List.of()))), "Other Cost", 1, 1, EAction.ACTION, new Spell.TalentCast(talent.stream().toList()), EnumSet.of(ECastingType.SOMATIC), 2, Set.of(), "Counter")
                ),
                Map.of()
        ), primaryAttributes, secondaryAttributes, talents);
    }

    private List<PnPCharacterDto> transform(String universe, List<PnPCharacter> characters) {
        Collection<PrimaryAttribute> primaryAttributes = primaryAttributeRepository.getAll(universe);
        Collection<SecondaryAttribute> secondaryAttributes = secondaryAttributeRepository.getAll(universe);
        Collection<Talent> talents = talentRepository.getAll(universe);

        List<PnPCharacterDto> dtos = new ArrayList<>();

        for (PnPCharacter character : characters) {
            dtos.add(transform(character, primaryAttributes, secondaryAttributes, talents));
        }

        return dtos;
    }

    private PnPCharacterDto transform(PnPCharacter character, Collection<PrimaryAttribute> primaryAttributes,
                                      Collection<SecondaryAttribute> secondaryAttributes,
                                      Collection<Talent> talents) {
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

        return new PnPCharacterDto(
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
                character.getInventory(),
                character.getSpells(),
                character.getCustomFields()
        );
    }
}
