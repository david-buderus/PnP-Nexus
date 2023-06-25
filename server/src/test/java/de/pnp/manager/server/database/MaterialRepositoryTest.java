package de.pnp.manager.server.database;

import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.Material;
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
    Item itemWithSpellingMistake = itemRepository.insert(universe,
        itemBuilder.someItem(universe).withName("Iron ingt").buildItem());
    Item item = itemBuilder.someItem(universe).withName("Iron ingot").buildItem();
    Material material = new Material(null, "Iron", List.of(itemWithSpellingMistake));

    testRepositoryCollectionLink(Material::getItems, itemRepository, material,
        List.of(itemWithSpellingMistake),
        Map.of(itemWithSpellingMistake, item));
  }

  @Override
  protected Material createObject() {
    Item ironIngot = itemRepository.insert(universe,
        itemBuilder.someItem(universe).withName("Iron ingot").buildItem());
    return new Material(null, "Iron", List.of(ironIngot));
  }

  @Override
  protected Material createSlightlyChangeObject() {
    Item ironNugget = itemRepository.insert(universe,
        itemBuilder.someItem(universe).withName("Iron nugget").buildItem());
    return new Material(null, "Iron", List.of(ironNugget));
  }
}
