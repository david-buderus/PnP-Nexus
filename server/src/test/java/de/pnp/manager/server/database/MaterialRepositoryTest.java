package de.pnp.manager.server.database;

import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.item.Material.MaterialItem;
import de.pnp.manager.server.database.item.ItemRepository;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests for {@link MaterialRepository}
 */
public class MaterialRepositoryTest extends RepositoryTestBase<Material, MaterialRepository> {

    @Autowired
    private ItemRepository itemRepository;

    public MaterialRepositoryTest(@Autowired MaterialRepository repository) {
        super(repository);
    }

    @Test
    void testItemLink() {
        Item itemWithSpellingMistake = itemRepository.insert(getUniverseName(),
            createItem().withName("Iron ingt").buildItem());
        Item item = createItem().withName("Iron ingot").buildItem();
        Material material = new Material(null, "Iron", List.of(new MaterialItem(1, itemWithSpellingMistake)));

        testRepositoryCollectionLink(m -> m.getItems().stream().map(MaterialItem::item).toList(), itemRepository,
            material,
            List.of(itemWithSpellingMistake),
            Map.of(itemWithSpellingMistake, item));
    }

    @Override
    protected Material createObject() {
        Item ironIngot = itemRepository.insert(getUniverseName(),
            createItem().withName("Iron ingot").buildItem());
        return new Material(null, "Iron", List.of(new MaterialItem(1, ironIngot)));
    }

    @Override
    protected Material createSlightlyChangeObject() {
        Item ironNugget = itemRepository.insert(getUniverseName(),
            createItem().withName("Iron nugget").buildItem());
        return new Material(null, "Iron", List.of(new MaterialItem(1, ironNugget)));
    }

    @Override
    protected List<Material> createMultipleObjects() {
        return List.of(new Material(null, "Iron", List.of()), new Material(null, "Copper", List.of()));
    }
}
