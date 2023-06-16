package de.pnp.manager.server.database;

import static de.pnp.manager.server.component.ItemBuilder.someItem;
import static org.assertj.core.api.Assertions.assertThat;

import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.equipable.Armor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests for {@link ItemRepository}.
 */
public class ItemRepositoryTest extends UniverseTestBase {

  @Autowired
  private ItemRepository itemRepository;

  @Test
  void testInsertItem() {
    Item item = someItem().buildItem();
    Item persistedItem = itemRepository.insert(universe, item);

    assertThat(persistedItem).isEqualTo(item);
    assertThat(itemRepository.getAll(universe)).contains(item);
    assertThat(itemRepository.get(universe, item.getName())).contains(item);
    assertThat(itemRepository.get(universe, persistedItem.getId())).contains(item);
  }

  @Test
  void testInsertArmor() {
    Armor armor = someItem().buildArmor();
    itemRepository.insert(universe, armor);

    assertThat(itemRepository.getAll(universe)).contains(armor);
    assertThat(itemRepository.get(universe, armor.getName())).contains(armor);
  }
}
