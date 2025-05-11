package de.pnp.manager.server.database;

import static org.assertj.core.api.Assertions.assertThat;

import de.pnp.manager.component.CraftingRecipe;
import de.pnp.manager.component.IResourceUsage.ItemUsage;
import de.pnp.manager.component.IResourceUsage.MaterialUsage;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.server.database.item.ItemRepository;
import java.util.Collections;
import java.util.List;
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
    void testResultLink() {
        Item resultOld = itemRepository.insert(getUniverseName(),
            createItem().withName("Result old").buildItem());
        Item resultNew = createItem().withName("Result new").buildItem();
        Item materialItem = itemRepository.insert(getUniverseName(),
            createItem().withName("A").buildItem());
        CraftingRecipe craftingRecipe = new CraftingRecipe(null, "", "", "",
            List.of(new ItemUsage(1, resultOld)), List.of(new ItemUsage(2, materialItem)));

        testRepositoryLink(recipe -> recipe.getProducts().getFirst().resource(), itemRepository, craftingRecipe,
            resultOld, resultNew);
    }


    @Test
    void testCraftingRecipeMaterial() {
        Item result = itemRepository.insert(getUniverseName(),
            createItem().withName("Result").buildItem());
        Material material = materialRepository.insert(getUniverseName(),
            new Material(null, "Material", Collections.emptyList()));

        CraftingRecipe craftingRecipe = new CraftingRecipe(null, "", "", "",
            List.of(new ItemUsage(1, result)), List.of(new MaterialUsage(7, material)));

        repository.insert(getUniverseName(), craftingRecipe);
        assertThat(repository.getAll(getUniverseName())).contains(craftingRecipe);
    }

    @Override
    protected CraftingRecipe createObject() {
        Item result = itemRepository.insert(getUniverseName(),
            createItem().withName("Result").buildItem());
        Item itemA = itemRepository.insert(getUniverseName(),
            createItem().withName("A").buildItem());
        Item itemB = itemRepository.insert(getUniverseName(),
            createItem().withName("B").buildItem());

        return new CraftingRecipe(null, "", "", "",
            List.of(new ItemUsage(1, result)), List.of(new ItemUsage(7, itemA),
            new ItemUsage(4, itemB)));
    }

    @Override
    protected CraftingRecipe createSlightlyChangeObject() {
        Item result = itemRepository.get(getUniverseName(), "Result").orElseThrow();
        Item itemA = itemRepository.get(getUniverseName(), "A").orElseThrow();
        Item itemB = itemRepository.get(getUniverseName(), "B").orElseThrow();

        return new CraftingRecipe(null, "Alchemy", "", "Fire",
            List.of(new ItemUsage(1, result)), List.of(new ItemUsage(7, itemA),
            new ItemUsage(4, itemB)));
    }

    @Override
    protected List<CraftingRecipe> createMultipleObjects() {
        Item itemA = itemRepository.insert(getUniverseName(),
            createItem().withName("A").buildItem());
        Item itemB = itemRepository.insert(getUniverseName(),
            createItem().withName("B").buildItem());

        CraftingRecipe recipeA = new CraftingRecipe(null, "", "", "", List.of(new ItemUsage(1, itemA)),
            List.of(new ItemUsage(4, itemB)));
        CraftingRecipe recipeB = new CraftingRecipe(null, "", "", "", List.of(new ItemUsage(1, itemB)),
            List.of(new ItemUsage(4, itemA)));

        return List.of(recipeA, recipeB);
    }
}