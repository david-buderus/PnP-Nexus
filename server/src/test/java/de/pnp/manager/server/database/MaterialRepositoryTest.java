package de.pnp.manager.server.database;

import static de.pnp.manager.server.component.ItemBuilder.someItem;
import static org.assertj.core.api.Assertions.assertThat;

import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.item.equipable.Armor;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
        someItem().withName("Iron ingt").buildItem());

    Material material = new Material(null, "Iron", List.of(itemWithSpellingMistake));
    repository.insert(universe, material);

    Item item = someItem().withName("Iron ingot").buildItem();
    itemRepository.update(universe, itemWithSpellingMistake.getId(), item);

    Optional<Material> optMaterial = repository.get(universe, material.getName());
    assertThat(optMaterial).isNotEmpty();
    assertThat(optMaterial.get().getItems()).hasSize(1);
    assertThat(optMaterial.get().getItems().stream().findFirst()).contains(item);
  }

  @Test
  void testMaterialLink() {
    Material material = repository.insert(universe,
        new Material(null, "Material A", Collections.emptyList()));

    Item armor = itemRepository.insert(universe,
        someItem().withName("Test").withMaterial(material).buildArmor());

    repository.update(universe, material.getId(),
        new Material(null, "Material B", Collections.emptyList()));

    Optional<Item> optionalArmor = itemRepository.get(universe, armor.getId());
    assertThat(optionalArmor).isNotEmpty();
    assertThat(optionalArmor.get()).isInstanceOf(Armor.class);
    assertThat(((Armor) optionalArmor.get()).getMaterial().getName()).isEqualTo("Material B");
  }

  @Override
  protected Material createObject() {
    Item ironIngot = itemRepository.insert(universe, someItem().withName("Iron ingot").buildItem());
    return new Material(null, "Iron", List.of(ironIngot));
  }

  @Override
  protected Material createSlightlyChangeObject() {
    Item ironNugget = itemRepository.insert(universe,
        someItem().withName("Iron nugget").buildItem());
    return new Material(null, "Iron", List.of(ironNugget));
  }
}
