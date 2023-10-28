package de.pnp.manager.server.service.item;

import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.server.database.MaterialRepository;
import de.pnp.manager.server.database.item.ItemRepository;
import de.pnp.manager.server.service.RepositoryServiceBaseTest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests for {@link ItemService}.
 */
public class ItemServiceTest extends RepositoryServiceBaseTest<Item, ItemRepository, ItemService> {

    @Autowired
    private MaterialRepository materialRepository;

    public ItemServiceTest(@Autowired ItemService itemService) {
        super(itemService, Item.class);
    }

    @Override
    protected List<Item> createObjects() {
        Material material = materialRepository.insert(getUniverseName(), new Material(null, "Material", List.of()));
        return List.of(
            createItem().withName("Item").buildItem(),
            createItem().withName("Weapon").withMaterial(material).buildWeapon(),
            createItem().withName("Shield").withMaterial(material).buildShield(),
            createItem().withName("Armor").withMaterial(material).buildArmor(),
            createItem().withName("Jewellery").withMaterial(material).buildJewellery()
        );
    }
}
