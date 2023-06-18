package de.pnp.manager.server.database;

import static de.pnp.manager.server.component.ItemBuilder.someItem;
import static org.assertj.core.api.Assertions.assertThat;

import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.equipable.Armor;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests for {@link ItemRepository}.
 */
public class ItemRepositoryTest extends RepositoryTestBase<Item, ItemRepository> {

  public ItemRepositoryTest(@Autowired ItemRepository repository) {
    super(repository);
  }


  @Test
  void testInsertArmor() {
    Armor armor = someItem().buildArmor();
    Item persistedArmor = repository.insert(universe, armor);

    assertThat(repository.getAll(universe)).contains(armor);
    assertThat(repository.get(universe, armor.getName())).contains(armor);
    assertThat(repository.get(universe, persistedArmor.getId())).contains(armor);
  }

  @Override
  protected Item createObject() {
    return someItem().withName("Test").buildItem();
  }

  @Override
  protected Item createSlightlyChangeObject() {
    return someItem().withName("Test Differently").buildItem();
  }

  @Override
  protected String getName(Item object) {
    return object.getName();
  }
}
