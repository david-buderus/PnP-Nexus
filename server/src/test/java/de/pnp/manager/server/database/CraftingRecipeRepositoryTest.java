package de.pnp.manager.server.database;

import static org.assertj.core.api.Assertions.assertThat;

import de.pnp.manager.component.CraftingRecipe;
import de.pnp.manager.component.CraftingRecipe.CraftingItem;
import de.pnp.manager.component.CraftingRecipe.CraftingMaterial;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.Material;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests for {@link CraftingRecipeRepository}.
 */
class CraftingRecipeRepositoryTest extends
    RepositoryTestBase<CraftingRecipe, CraftingRecipeRepository> {

  @Autowired
  private ItemRepository itemRepository;

  @Autowired
  private MaterialRepository materialRepository;

  public CraftingRecipeRepositoryTest(@Autowired CraftingRecipeRepository repository) {
    super(repository);
  }

  @Test
  void testChangeCraftingRecipeResult() {
    Item resultOld = itemRepository.insert(universe,
        itemBuilder.createItemBuilder(universe).withName("Result old").buildItem());
    Item itemA = itemRepository.insert(universe,
        itemBuilder.createItemBuilder(universe).withName("A").buildItem());

    CraftingRecipe craftingRecipe = new CraftingRecipe(null, "", "", "",
        new CraftingItem(1, resultOld), null, List.of(new CraftingItem(2, itemA)));

    repository.insert(universe, craftingRecipe);
    assertThat(repository.getAll(universe)).contains(craftingRecipe);

    Item resultNew = itemBuilder.createItemBuilder(universe).withName("Result new").buildItem();
    itemRepository.update(universe, resultOld.getId(), resultNew);

    Optional<CraftingRecipe> optFabrication = repository.getAll(universe).stream()
        .findFirst();
    assertThat(optFabrication).isNotEmpty();
    assertThat(optFabrication.get().getProduct().item()).isEqualTo(resultNew);
  }


  @Test
  void testCraftingRecipeMaterial() {
    Item result = itemRepository.insert(universe,
        itemBuilder.createItemBuilder(universe).withName("Result").buildItem());
    Material material = materialRepository.insert(universe,
        new Material(null, "Material", Collections.emptyList()));

    CraftingRecipe craftingRecipe = new CraftingRecipe(null, "", "", "",
        new CraftingItem(1, result),
        null, List.of(new CraftingMaterial(7, material)));

    repository.insert(universe, craftingRecipe);
    assertThat(repository.getAll(universe)).contains(craftingRecipe);
  }

  @Override
  protected CraftingRecipe createObject() {
    Item result = itemRepository.insert(universe,
        itemBuilder.createItemBuilder(universe).withName("Result").buildItem());
    Item itemA = itemRepository.insert(universe,
        itemBuilder.createItemBuilder(universe).withName("A").buildItem());
    Item itemB = itemRepository.insert(universe,
        itemBuilder.createItemBuilder(universe).withName("B").buildItem());

    return new CraftingRecipe(null, "", "", "",
        new CraftingItem(1, result),
        null, List.of(new CraftingItem(7, itemA),
        new CraftingItem(4, itemB)));
  }

  @Override
  protected CraftingRecipe createSlightlyChangeObject() {
    Item result = itemRepository.get(universe, "Result").orElseThrow();
    Item itemA = itemRepository.get(universe, "A").orElseThrow();
    Item itemB = itemRepository.get(universe, "B").orElseThrow();

    return new CraftingRecipe(null, "Alchemy", "", "Fire",
        new CraftingItem(1, result),
        null, List.of(new CraftingItem(7, itemA),
        new CraftingItem(4, itemB)));
  }

  @Override
  protected Collection<CraftingRecipe> createMultipleObjects() {
    Item itemA = itemRepository.insert(universe,
        itemBuilder.createItemBuilder(universe).withName("A").buildItem());
    Item itemB = itemRepository.insert(universe,
        itemBuilder.createItemBuilder(universe).withName("B").buildItem());

    CraftingRecipe recipeA = new CraftingRecipe(null, "", "", "", new CraftingItem(1, itemA),
        null, List.of(new CraftingItem(4, itemB)));
    CraftingRecipe recipeB = new CraftingRecipe(null, "", "", "", new CraftingItem(1, itemB),
        null, List.of(new CraftingItem(4, itemA)));

    return List.of(recipeA, recipeB);
  }
}