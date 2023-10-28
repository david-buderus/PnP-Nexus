package de.pnp.manager.server.service.item;

import de.pnp.manager.component.item.ItemType;
import de.pnp.manager.component.item.ItemType.ETypeRestriction;
import de.pnp.manager.server.database.item.ItemTypeRepository;
import de.pnp.manager.server.service.RepositoryServiceBaseTest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests for {@link ItemTypeService}.
 */
public class ItemTypeServiceTest extends RepositoryServiceBaseTest<ItemType, ItemTypeRepository, ItemTypeService> {

    public ItemTypeServiceTest(@Autowired ItemTypeService itemTypeService) {
        super(itemTypeService, ItemType.class);
    }

    @Override
    protected List<ItemType> createObjects() {
        return List.of(
            new ItemType(null, "Material", ETypeRestriction.ITEM),
            new ItemType(null, "Sword", ETypeRestriction.WEAPON),
            new ItemType(null, "Helmet", ETypeRestriction.ARMOR),
            new ItemType(null, "Tool", ETypeRestriction.HANDHELD),
            new ItemType(null, "Ring", ETypeRestriction.JEWELLERY),
            new ItemType(null, "Great Shield", ETypeRestriction.SHIELD),
            new ItemType(null, "Equipment", ETypeRestriction.EQUIPMENT),
            new ItemType(null, "Defensive", ETypeRestriction.DEFENSIVE_ITEM)
        );
    }
}
