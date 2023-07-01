package de.pnp.manager.server.database.item;

import de.pnp.manager.component.item.ItemType;
import de.pnp.manager.component.item.ItemType.TypeRestriction;
import de.pnp.manager.server.database.RepositoryTestBase;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests for {@link ItemTypeRepository}
 */
class ItemTypeRepositoryTest extends RepositoryTestBase<ItemType, ItemTypeRepository> {

    public ItemTypeRepositoryTest(@Autowired ItemTypeRepository repository) {
        super(repository);
    }

    @Override
    protected ItemType createObject() {
        return new ItemType(null, "Weapon", TypeRestriction.ITEM);
    }

    @Override
    protected ItemType createSlightlyChangeObject() {
        return new ItemType(null, "Weapon", TypeRestriction.WEAPON);
    }

    @Override
    protected Collection<ItemType> createMultipleObjects() {
        return List.of(new ItemType(null, "Armor", TypeRestriction.ARMOR),
            new ItemType(null, "Shield", TypeRestriction.SHIELD));
    }
}
