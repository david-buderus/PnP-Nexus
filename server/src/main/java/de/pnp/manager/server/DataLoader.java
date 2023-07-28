package de.pnp.manager.server;

import de.pnp.manager.component.Universe;
import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.ItemType;
import de.pnp.manager.component.item.ItemType.ETypeRestriction;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.server.database.MaterialRepository;
import de.pnp.manager.server.database.UniverseRepository;
import de.pnp.manager.server.database.item.ItemRepository;
import de.pnp.manager.server.database.item.ItemTypeRepository;
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

        ItemType material = typeRepository.insert(TEST_UNIVERSE, new ItemType(null, "Material", ETypeRestriction.ITEM));
        ItemType metal = typeRepository.insert(TEST_UNIVERSE, new ItemType(null, "Metal", ETypeRestriction.ITEM));
        Item ironIngot = itemRepository.insert(TEST_UNIVERSE,
            new Item(null, "Iron ingot", material, metal, "", "", ERarity.COMMON, 100, 1, "An ingot of iron.", "", 100,
                0));
        materialRepository.insert(TEST_UNIVERSE, new Material(null, "Iron", List.of(ironIngot)));
    }
}
