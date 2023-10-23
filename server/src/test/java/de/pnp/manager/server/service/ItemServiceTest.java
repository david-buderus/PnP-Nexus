package de.pnp.manager.server.service;

import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.server.database.MaterialRepository;
import de.pnp.manager.server.database.TestItemBuilder;
import de.pnp.manager.server.database.TestItemBuilder.TestItemBuilderFactory;
import de.pnp.manager.server.database.item.ItemRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests for {@link ItemService}.
 */
public class ItemServiceTest extends RepositoryServiceBaseTest<Item, ItemRepository, ItemService> {

    @Autowired
    private TestItemBuilderFactory itemBuilder;

    @Autowired
    private MaterialRepository materialRepository;

    public ItemServiceTest(@Autowired ItemService itemService) {
        super(itemService, Item.class);
    }

    @Override
    protected List<Item> createObjects() {
        Material material = materialRepository.insert(universeName, new Material(null, "Material", List.of()));
        return List.of(
            createItem().withName("Item").buildItem(),
            createItem().withName("Weapon").withMaterial(material).buildWeapon(),
            createItem().withName("Shield").withMaterial(material).buildShield(),
            createItem().withName("Armor").withMaterial(material).buildArmor(),
            createItem().withName("Jewellery").withMaterial(material).buildJewellery()
        );
    }

    /**
     * A helper method to create {@link TestItemBuilder}.
     */
    protected TestItemBuilder createItem() {
        return itemBuilder.createItemBuilder(universeName);
    }
}
