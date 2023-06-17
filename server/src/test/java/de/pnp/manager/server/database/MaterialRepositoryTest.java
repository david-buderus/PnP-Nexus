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
public class MaterialRepositoryTest extends UniverseTestBase {

  @Autowired
  private MaterialRepository materialRepository;

  @Autowired
  private ItemRepository itemRepository;

  @Test
  void testInsertMaterial() {
    Item ironIngot = itemRepository.insert(universe, someItem().withName("Iron ingot").buildItem());

    Material material = new Material(null, "Iron", List.of(ironIngot));
    Material persistedMaterial = materialRepository.insert(universe, material);

    assertThat(material).isEqualTo(persistedMaterial);
    assertThat(materialRepository.getAll(universe)).contains(material);
    assertThat(materialRepository.get(universe, material.getName())).contains(material);
    assertThat(materialRepository.get(universe, persistedMaterial.getId())).contains(material);
  }


  @Test
  void testItemLink() {
    Item itemWithSpellingMistake = itemRepository.insert(universe,
        someItem().withName("Iron ingt").buildItem());

    Material material = new Material(null, "Iron", List.of(itemWithSpellingMistake));
    materialRepository.insert(universe, material);

    Item item = someItem().withName("Iron ingot").buildItem();
    itemRepository.update(universe, itemWithSpellingMistake.getId(), item);

    Optional<Material> optMaterial = materialRepository.get(universe, material.getName());
    assertThat(optMaterial).isNotEmpty();
    assertThat(optMaterial.get().getItems()).hasSize(1);
    assertThat(optMaterial.get().getItems().stream().findFirst()).contains(item);
  }

  @Test
  void testMaterialLink() {
    Material material = materialRepository.insert(universe,
        new Material(null, "Material A", Collections.emptyList()));

    Item armor = itemRepository.insert(universe,
        someItem().withName("Test").withMaterial(material).buildArmor());

    materialRepository.update(universe, material.getId(),
        new Material(null, "Material B", Collections.emptyList()));

    Optional<Item> optionalArmor = itemRepository.get(universe, armor.getId());
    assertThat(optionalArmor).isNotEmpty();
    assertThat(optionalArmor.get()).isInstanceOf(Armor.class);
    assertThat(((Armor) optionalArmor.get()).getMaterial().getName()).isEqualTo("Material B");
  }
}
