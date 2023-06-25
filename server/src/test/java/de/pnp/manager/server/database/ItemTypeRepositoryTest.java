package de.pnp.manager.server.database;

import de.pnp.manager.component.item.ItemType;
import de.pnp.manager.component.item.ItemType.TypeRestriction;
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
}
