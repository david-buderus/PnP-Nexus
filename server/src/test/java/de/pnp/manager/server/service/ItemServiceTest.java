package de.pnp.manager.server.service;

import de.pnp.manager.component.item.Item;
import de.pnp.manager.server.component.TestItemBuilder;
import de.pnp.manager.server.component.TestItemBuilder.TestItemBuilderFactory;
import de.pnp.manager.server.database.ItemRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests for {@link ItemService}.
 */
public class ItemServiceTest extends RepositoryServiceBaseTest<Item, ItemRepository, ItemService> {

    @Autowired
    private TestItemBuilderFactory itemBuilder;

    public ItemServiceTest(@Autowired ItemService itemService) {
        super(itemService);
    }

    @Override
    protected List<Item> createObjects() {
        return List.of(createItem().withName("A").buildItem(), createItem().withName("B").buildItem(),
            createItem().withName("C").buildItem(), createItem().withName("D").buildItem());
    }

    /**
     * A helper method to create {@link TestItemBuilder}.
     */
    protected TestItemBuilder createItem() {
        return itemBuilder.createItemBuilder(universeName);
    }
}
