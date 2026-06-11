package de.pnp.manager.server.service.character;

import de.pnp.manager.Tag;
import de.pnp.manager.component.Dice;
import de.pnp.manager.component.EAction;
import de.pnp.manager.component.ECalculation;
import de.pnp.manager.component.IResourceUsage;
import de.pnp.manager.component.attributes.PrimaryAttribute;
import de.pnp.manager.component.attributes.SecondaryAttribute;
import de.pnp.manager.component.character.*;
import de.pnp.manager.component.character.dto.PnPCharacterDTO;
import de.pnp.manager.component.character.stats.Stat;
import de.pnp.manager.component.character.traits.SimpleCharacterTrait;
import de.pnp.manager.component.character.traits.StatTrait;
import de.pnp.manager.component.inventory.Inventory;
import de.pnp.manager.component.inventory.ItemStack;
import de.pnp.manager.component.inventory.equipment.ArmorEquipment;
import de.pnp.manager.component.inventory.equipment.JewelleryEquipment;
import de.pnp.manager.component.inventory.equipment.ShieldEquipment;
import de.pnp.manager.component.inventory.equipment.WeaponEquipment;
import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.item.equipable.*;
import de.pnp.manager.component.math.EReservedVariables;
import de.pnp.manager.component.math.IExpressionVariable;
import de.pnp.manager.component.spell.ECastingType;
import de.pnp.manager.component.spell.Spell;
import de.pnp.manager.component.universe.CharacterSettings;
import de.pnp.manager.component.universe.EquipmentSettings;
import de.pnp.manager.security.UniverseRead;
import de.pnp.manager.server.contoller.PnPCharacterDTOConverter;
import de.pnp.manager.server.database.TalentRepository;
import de.pnp.manager.server.database.attributes.PrimaryAttributeRepository;
import de.pnp.manager.server.database.attributes.SecondaryAttributeRepository;
import de.pnp.manager.server.database.character.PnPCharacterSheetRepository;
import de.pnp.manager.server.database.universe.UniverseSettingsRepository;
import de.pnp.manager.server.service.RepositoryServiceBase;
import io.swagger.v3.oas.annotations.Operation;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service to access {@link PnPCharacterSheetRepository}.
 */
@RestController
@RequestMapping("api/{universe}/character-sheets")
public class PnPCharacterSheetService extends RepositoryServiceBase<PnPCharacterSheet, PnPCharacterSheetRepository> {

    private final PrimaryAttributeRepository primaryAttributeRepository;
    private final SecondaryAttributeRepository secondaryAttributeRepository;
    private final TalentRepository talentRepository;
    private final UniverseSettingsRepository universeSettingsRepository;

    private final PnPCharacterDTOConverter controller;

    public PnPCharacterSheetService(
            @Autowired PnPCharacterSheetRepository repository,
            @Autowired PrimaryAttributeRepository primaryAttributeRepository,
            @Autowired SecondaryAttributeRepository secondaryAttributeRepository,
            @Autowired TalentRepository talentRepository,
            @Autowired UniverseSettingsRepository universeSettingsRepository,
            @Autowired PnPCharacterDTOConverter controller) {
        super(repository);
        this.primaryAttributeRepository = primaryAttributeRepository;
        this.secondaryAttributeRepository = secondaryAttributeRepository;
        this.talentRepository = talentRepository;
        this.universeSettingsRepository = universeSettingsRepository;
        this.controller = controller;
    }

    @GetMapping("example-character")
    @UniverseRead
    @Operation(summary = "Generates an example character", operationId = "getExampleCharacter")
    public PnPCharacterDTO getExampleCharacter(@PathVariable ObjectId universe) {
        Collection<PrimaryAttribute> primaryAttributes = primaryAttributeRepository.getAll(universe);
        Collection<SecondaryAttribute> secondaryAttributes = secondaryAttributeRepository.getAll(universe);
        Collection<Talent> talents = talentRepository.getAll(universe);
        CharacterSettings characterSettings = universeSettingsRepository.getSettings(universe, CharacterSettings.class);
        EquipmentSettings equipmentSettings = universeSettingsRepository.getSettings(universe, EquipmentSettings.class);

        CharacterStats stats = new CharacterStats(
                primaryAttributes.stream()
                        .collect(Collectors.toMap(PrimaryAttribute::getId, a -> new Stat(5))),
                secondaryAttributes.stream()
                        .collect(Collectors.toMap(SecondaryAttribute::getId, a -> new Stat(2)))
        );
        stats.recalculateSecondaryStats(primaryAttributes, secondaryAttributes, Map.of(
                new IExpressionVariable.StringVariable(EReservedVariables.LEVEL.getConstant()), 1d,
                new IExpressionVariable.StringVariable(EReservedVariables.TIER.getConstant()), 1d
        ));

        Optional<Talent> talent = talents.stream().findFirst();

        Map<String, List<JewelleryEquipment>> jewellery = new HashMap<>();
        if (!equipmentSettings.getJewelleryDefinitions().isEmpty()) {
            EquipmentSettings.JewelleryDefinition definition = equipmentSettings.getJewelleryDefinitions().getFirst();
            jewellery.put(definition.name(), List.of(new JewelleryEquipment(1,
                    new Jewellery(null, "Jewellery", Set.of(Tag.from(definition.tag())), "", List.of(), ERarity.COMMON, 200, 1, "", "", null, 1, 1, 1)
            )));
        }

        Map<String, Inventory> inventories = new HashMap<>();
        for (CharacterSettings.InventorySizeEntry entry : characterSettings.getInventorySizes()) {
            inventories.put(entry.name(), new Inventory(entry.size(), List.of(
                            new ItemStack<>(5, new Item(null, "Item", Set.of(), "", List.of(), ERarity.COMMON, 54, 2, "", "", 100, 0, 0))
                    ))
            );
        }

        return controller.convert(universe, new PnPCharacter(
                null,
                new CharacterDescription("Name", "Profession", "Male", "Backstory", "Appearance", "Personality", "Goals", "Deficits", "Affiliations"),
                new CharacterLevel(2, 1, 0),
                new CharacterOrigin(
                        new Species(null, "Race", "Race description", true, List.of(), List.of(), List.of()),
                        null
                ),
                List.of(
                        new SimpleCharacterTrait("Some Advantage")
                ),
                List.of(
                        new StatTrait.SecondaryStatTrait(ECalculation.MULTIPLICATIVE, 10, secondaryAttributes.stream().findFirst().orElseThrow(), "Some Disadvantage")
                ),
                stats,
                new CharacterTalents(talent.stream().collect(Collectors.toMap(Talent::getId, t -> 2))),
                new CharacterEquipment(
                        List.of(new WeaponEquipment(1, new Weapon(null, "Weapon", Set.of(), "", List.of(), ERarity.COMMON, 100, 1, "", "", null, 2, 1, 0, 1, Dice.simpleDice(6), 1, 1), 0)),
                        List.of(new WeaponEquipment(1, new Weapon(null, "Fallback Weapon", Set.of(), "", List.of(), ERarity.COMMON, 100, 1, "", "", null, 2, 1, 0, 1, Dice.simpleDice(6), 1, 1), 0)),
                        List.of(new ShieldEquipment(1, new Shield(null, "Shield", Set.of(), "", List.of(), ERarity.COMMON, 100, 1, "", "", null, 2, 1, 0, Dice.simpleDice(6), 1, 1, 2, 1, 1), 0)),
                        List.of(new ShieldEquipment(1, new Shield(null, "Fallback Shield", Set.of(), "", List.of(), ERarity.COMMON, 100, 1, "", "", null, 2, 1, 0, Dice.simpleDice(6), 1, 1, 2, 1, 1), 0)),
                        Map.of(EArmorSlot.BODY, new ArmorEquipment(1, new Armor(null, "Body", Set.of(), "", List.of(), ERarity.COMMON, 100, 1, "", "", null, 1, EArmorSlot.BODY, 3, 2, 1, 1, 1), 0)),
                        jewellery
                ),
                new CharacterInventory(inventories, 52135),
                List.of(
                        new Spell(null, "Spell", "Effect", List.of(new IResourceUsage.MaterialUsage(1, new Material(null, "Material", List.of()))), "Other Cost", 1, 1, EAction.ACTION, new Spell.TalentCast(talent.stream().toList()), EnumSet.of(ECastingType.SOMATIC), 2, Set.of(), "Counter")
                ),
                Map.of()
        ));
    }
}
