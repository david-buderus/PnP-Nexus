package de.pnp.manager.server.database;

import static de.pnp.manager.server.component.ItemBuilder.someItem;
import static org.assertj.core.api.Assertions.assertThat;

import de.pnp.manager.component.CraftingRecipe;
import de.pnp.manager.component.CraftingRecipe.CraftingItem;
import de.pnp.manager.component.CraftingRecipe.CraftingMaterial;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.Material;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests for {@link CraftingRecipeRepository}.
 */
class CraftingRecipeRepositoryTest extends UniverseTestBase {

  @Autowired
  private ItemRepository itemRepository;

  @Autowired
  private MaterialRepository materialRepository;

  @Autowired
  private CraftingRecipeRepository craftingRecipeRepository;

  @Test
  void testInsertCraftingRecipe() {
    Item result = itemRepository.insert(universe, someItem().withName("Result").buildItem());
    Item itemA = itemRepository.insert(universe, someItem().withName("A").buildItem());
    Item itemB = itemRepository.insert(universe, someItem().withName("B").buildItem());

    CraftingRecipe craftingRecipe = new CraftingRecipe(null, "", "", "",
        new CraftingItem(1, result),
        null, List.of(new CraftingItem(7, itemA),
        new CraftingItem(4, itemB)));

    craftingRecipeRepository.insert(universe, craftingRecipe);
    assertThat(craftingRecipeRepository.getAll(universe)).contains(craftingRecipe);
  }

  @Test
  void testChangeCraftingRecipeResult() {
    Item resultOld = itemRepository.insert(universe, someItem().withName("Result old").buildItem());
    Item itemA = itemRepository.insert(universe, someItem().withName("A").buildItem());

    CraftingRecipe craftingRecipe = new CraftingRecipe(null, "", "", "",
        new CraftingItem(1, resultOld), null, List.of(new CraftingItem(2, itemA)));

    craftingRecipeRepository.insert(universe, craftingRecipe);
    assertThat(craftingRecipeRepository.getAll(universe)).contains(craftingRecipe);

    Item resultNew = someItem().withName("Result new").buildItem();
    itemRepository.update(universe, resultOld.getId(), resultNew);

    Optional<CraftingRecipe> optFabrication = craftingRecipeRepository.getAll(universe).stream()
        .findFirst();
    assertThat(optFabrication).isNotEmpty();
    assertThat(optFabrication.get().getProduct().item()).isEqualTo(resultNew);
  }


  @Test
  void testCraftingRecipeMaterial() {
    Item result = itemRepository.insert(universe, someItem().withName("Result").buildItem());
    Material material = materialRepository.insert(universe,
        new Material(null, "Material", Collections.emptyList()));

    CraftingRecipe craftingRecipe = new CraftingRecipe(null, "", "", "",
        new CraftingItem(1, result),
        null, List.of(new CraftingMaterial(7, material)));

    craftingRecipeRepository.insert(universe, craftingRecipe);
    assertThat(craftingRecipeRepository.getAll(universe)).contains(craftingRecipe);
  }
}