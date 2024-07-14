package de.pnp.manager.server.service.universe;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.ExtendedItemType;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.ItemType;
import de.pnp.manager.component.item.ItemType.ETypeRestriction;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.item.Material.MaterialItem;
import de.pnp.manager.component.universe.CharacterSettings.ArmorDefinition;
import de.pnp.manager.component.universe.CharacterSettings.JewelleryDefinition;
import de.pnp.manager.component.universe.Universe;
import de.pnp.manager.security.UniverseOwner;
import de.pnp.manager.server.contoller.ExtendedItemTypeController;
import de.pnp.manager.server.database.MaterialRepository;
import de.pnp.manager.server.database.item.ItemRepository;
import de.pnp.manager.server.database.item.ItemTypeRepository;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Service to help create {@link Universe universes}.
 */
@RestController
@Validated
@RequestMapping("/api/{universe}/universe-creation")
public class UniverseCreationService {

    @Autowired
    private ExtendedItemTypeController itemTypeController;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private ItemTypeRepository itemTypeRepository;

    @PostMapping("equipment-types")
    @UniverseOwner
    @Operation(summary = "Creates the basic item types most universes need", operationId = "createDefaultItemTypes")
    public void createDefaultEquipmentTypes(@PathVariable String universe, @RequestParam String language) {
        ResourceBundle bundle = ResourceBundle.getBundle("universeCreation", new Locale(language));

        ExtendedItemType equipment = itemTypeController.insert(universe,
            new ExtendedItemType(null, bundle.getString("equipment"), ETypeRestriction.EQUIPMENT, null,
                List.of())
        );
        ExtendedItemType jewellery = itemTypeController.insert(universe,
            new ExtendedItemType(null, bundle.getString("jewellery"), ETypeRestriction.JEWELLERY, null,
                List.of(equipment))
        );
        ExtendedItemType defensivItem = itemTypeController.insert(universe,
            new ExtendedItemType(null, bundle.getString("defensive_item"), ETypeRestriction.DEFENSIVE_ITEM,
                null,
                List.of(equipment))
        );
        ExtendedItemType handheld = itemTypeController.insert(universe,
            new ExtendedItemType(null, bundle.getString("handheld_item"), ETypeRestriction.HANDHELD, null,
                List.of(equipment))
        );
        ExtendedItemType armor = itemTypeController.insert(universe,
            new ExtendedItemType(null, bundle.getString("armor"), ETypeRestriction.ARMOR, null,
                List.of(defensivItem)));
        itemTypeController.insertAll(universe, List.of(
            new ExtendedItemType(null, bundle.getString("weapon"), ETypeRestriction.WEAPON, null,
                List.of(handheld)),
            new ExtendedItemType(null, bundle.getString("shield"), ETypeRestriction.SHIELD, null,
                List.of(defensivItem, handheld)),
            new ExtendedItemType(null, bundle.getString("head"), ETypeRestriction.ARMOR, null,
                List.of(armor)),
            new ExtendedItemType(null, bundle.getString("upperBody"), ETypeRestriction.ARMOR, null,
                List.of(armor)),
            new ExtendedItemType(null, bundle.getString("arms"), ETypeRestriction.ARMOR, null,
                List.of(armor)),
            new ExtendedItemType(null, bundle.getString("legs"), ETypeRestriction.ARMOR, null,
                List.of(armor)),
            new ExtendedItemType(null, bundle.getString("ring"), ETypeRestriction.JEWELLERY, null,
                List.of(jewellery)),
            new ExtendedItemType(null, bundle.getString("necklace"), ETypeRestriction.JEWELLERY, null,
                List.of(jewellery)),
            new ExtendedItemType(null, bundle.getString("bracelet"), ETypeRestriction.JEWELLERY, null,
                List.of(jewellery))
        ));
    }

    @GetMapping("equipment-types/armor-definitions")
    @UniverseOwner
    @Operation(summary = "Gets the basic armor definitions most universes need", operationId = "getDefaultArmorDefinitions")
    public List<ArmorDefinition> getDefaultArmorDefinitions(@PathVariable String universe,
        @RequestParam String language) {
        ResourceBundle bundle = ResourceBundle.getBundle("universeCreation", new Locale(language));

        String localizedHead = bundle.getString("head");
        ItemType head = itemTypeRepository.get(universe, localizedHead)
            .orElseThrow(this::missingPrerequisites);
        String localizedUpperBody = bundle.getString("upperBody");
        ItemType upperBody = itemTypeRepository.get(universe, localizedUpperBody)
            .orElseThrow(this::missingPrerequisites);
        String localizedArms = bundle.getString("arms");
        ItemType arms = itemTypeRepository.get(universe, localizedArms)
            .orElseThrow(this::missingPrerequisites);
        String localizedLegs = bundle.getString("legs");
        ItemType legs = itemTypeRepository.get(universe, localizedLegs)
            .orElseThrow(this::missingPrerequisites);

        return List.of(
            new ArmorDefinition(localizedHead, head),
            new ArmorDefinition(localizedUpperBody, upperBody),
            new ArmorDefinition(localizedArms, arms),
            new ArmorDefinition(localizedLegs, legs)
        );
    }

    @GetMapping("equipment-types/jewellery-definitions")
    @UniverseOwner
    @Operation(summary = "Gets the basic armor definitions most universes need", operationId = "getDefaultJewelleryDefinitions")
    public List<JewelleryDefinition> getDefaultJewelleryDefinitions(@PathVariable String universe,
        @RequestParam String language) {
        ResourceBundle bundle = ResourceBundle.getBundle("universeCreation", new Locale(language));

        String localizedRing = bundle.getString("ring");
        ItemType ring = itemTypeRepository.get(universe, localizedRing)
            .orElseThrow(this::missingPrerequisites);
        String localizedNecklace = bundle.getString("necklace");
        ItemType necklace = itemTypeRepository.get(universe, localizedNecklace)
            .orElseThrow(this::missingPrerequisites);
        String localizedBracelet = bundle.getString("bracelet");
        ItemType bracelet = itemTypeRepository.get(universe, localizedBracelet)
            .orElseThrow(this::missingPrerequisites);

        return List.of(
            new JewelleryDefinition(localizedRing, ring, 8),
            new JewelleryDefinition(localizedNecklace, necklace, 1),
            new JewelleryDefinition(localizedBracelet, bracelet, 2)
        );
    }

    @PostMapping("materials")
    @UniverseOwner
    @Operation(summary = "Creates a few basic materials most universes need", operationId = "createDefaultMaterials")
    public void createDefaultMaterials(@PathVariable String universe, String language) {
        ResourceBundle bundle = ResourceBundle.getBundle("universeCreation", new Locale(language));

        ExtendedItemType material = itemTypeController.insert(universe,
            new ExtendedItemType(null, bundle.getString("material"), ETypeRestriction.ITEM, null,
                List.of())
        );
        ExtendedItemType ore = itemTypeController.insert(universe,
            new ExtendedItemType(null, bundle.getString("ore"), ETypeRestriction.ITEM, null,
                List.of(material))
        );
        ExtendedItemType ingot = itemTypeController.insert(universe,
            new ExtendedItemType(null, bundle.getString("ingot"), ETypeRestriction.ITEM, null,
                List.of(material))
        );
        ExtendedItemType wood = itemTypeController.insert(universe,
            new ExtendedItemType(null, bundle.getString("wood"), ETypeRestriction.ITEM, null,
                List.of(material))
        );

        Item ironIngot = itemRepository.insert(universe,
            new Item(null, bundle.getString("iron_ingot"), material, ingot, "", "", ERarity.COMMON, 100, 1,
                bundle.getString("iron_ingot_description"), "", 0, 100));
        itemRepository.insert(universe,
            new Item(null, bundle.getString("iron_ore"), material, ore, "", "", ERarity.COMMON, 30, 1,
                bundle.getString("iron_ore_description"), "", 0, 100));
        Item woodPlank = itemRepository.insert(universe,
            new Item(null, bundle.getString("wood_plank"), material, wood, "", "", ERarity.COMMON, 10, 1,
                bundle.getString("wood_plank_description"), "", 0, 100));
        Item rawWood = itemRepository.insert(universe,
            new Item(null, bundle.getString("raw_wood"), material, wood, "", "", ERarity.COMMON, 3, 1,
                bundle.getString("raw_wood_description"), "", 0, 100));

        materialRepository.insertAll(universe, List.of(
            new Material(null, bundle.getString("iron"), List.of(new MaterialItem(1, ironIngot))),
            new Material(null, bundle.getString("wood"),
                List.of(new MaterialItem(1, woodPlank), new MaterialItem(1, rawWood)))
        ));
    }

    private ResponseStatusException missingPrerequisites() {
        return new ResponseStatusException(BAD_REQUEST, "Prerequisites not met");
    }
}
