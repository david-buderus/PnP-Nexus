package de.pnp.manager.server;

import de.pnp.manager.component.Universe;
import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.ItemType;
import de.pnp.manager.component.item.ItemType.TypeRestriction;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.server.database.ItemRepository;
import de.pnp.manager.server.database.ItemTypeRepository;
import de.pnp.manager.server.database.MaterialRepository;
import de.pnp.manager.server.database.UniverseRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * A helper class which fills the database with a few objects, if the server is started in DEV mode.
 */
@Component
public class DataLoader implements ApplicationRunner {

    private static final String TEST_UNIVERSE = "test";

    @Autowired
    private UniverseRepository universeRepository;

    @Autowired
    private ItemTypeRepository typeRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (!args.containsOption("DEV")) {
            return;
        }
        if (universeRepository.exists(TEST_UNIVERSE)) {
            universeRepository.remove(TEST_UNIVERSE);
        }
        universeRepository.insert(new Universe(TEST_UNIVERSE, TEST_UNIVERSE));

        ItemType material = typeRepository.insert(TEST_UNIVERSE, new ItemType(null, "Material", TypeRestriction.ITEM));
        ItemType metal = typeRepository.insert(TEST_UNIVERSE, new ItemType(null, "Metal", TypeRestriction.ITEM));
        Item ironIngot = itemRepository.insert(TEST_UNIVERSE, new Item(null, "Iron ingot", material, metal, "", "", ERarity.COMMON, 100, 1, "An ingot of iron.", ""));
        materialRepository.insert(TEST_UNIVERSE, new Material(null, "Iron", List.of(ironIngot)));
    }
}
