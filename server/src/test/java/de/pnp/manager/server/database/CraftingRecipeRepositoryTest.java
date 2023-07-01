package de.pnp.manager.server.database;

import static org.assertj.core.api.Assertions.assertThat;

import de.pnp.manager.component.CraftingRecipe;
import de.pnp.manager.component.IRecipeEntry.ItemRecipeEntry;
import de.pnp.manager.component.IRecipeEntry.MaterialRecipeEntry;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.server.database.item.ItemRepository;
import java.util.Collection;
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
        Item resultOld = itemRepository.insert(universeName,
            itemBuilder.createItemBuilder(universeName).withName("Result old").buildItem());
        Item resultNew = itemBuilder.createItemBuilder(universeName).withName("Result new").buildItem();
        Item materialItem = itemRepository.insert(universeName,
            itemBuilder.createItemBuilder(universeName).withName("A").buildItem());
        CraftingRecipe craftingRecipe = new CraftingRecipe(null, "", "", "",
            new ItemRecipeEntry(1, resultOld), null, List.of(new ItemRecipeEntry(2, materialItem)));

        testRepositoryLink(recipe -> recipe.getProduct().item(), itemRepository, craftingRecipe, resultOld, resultNew);
    }


    @Test
    void testCraftingRecipeMaterial() {
        Item result = itemRepository.insert(universeName,
            itemBuilder.createItemBuilder(universeName).withName("Result").buildItem());
        Material material = materialRepository.insert(universeName,
            new Material(null, "Material", Collections.emptyList()));

        CraftingRecipe craftingRecipe = new CraftingRecipe(null, "", "", "",
            new ItemRecipeEntry(1, result),
            null, List.of(new MaterialRecipeEntry(7, material)));

        repository.insert(universeName, craftingRecipe);
        assertThat(repository.getAll(universeName)).contains(craftingRecipe);
    }

    @Override
    protected CraftingRecipe createObject() {
        Item result = itemRepository.insert(universeName,
            itemBuilder.createItemBuilder(universeName).withName("Result").buildItem());
        Item itemA = itemRepository.insert(universeName,
            itemBuilder.createItemBuilder(universeName).withName("A").buildItem());
        Item itemB = itemRepository.insert(universeName,
            itemBuilder.createItemBuilder(universeName).withName("B").buildItem());

        return new CraftingRecipe(null, "", "", "",
            new ItemRecipeEntry(1, result),
            null, List.of(new ItemRecipeEntry(7, itemA),
            new ItemRecipeEntry(4, itemB)));
    }

    @Override
    protected CraftingRecipe createSlightlyChangeObject() {
        Item result = itemRepository.get(universeName, "Result").orElseThrow();
        Item itemA = itemRepository.get(universeName, "A").orElseThrow();
        Item itemB = itemRepository.get(universeName, "B").orElseThrow();

        return new CraftingRecipe(null, "Alchemy", "", "Fire",
            new ItemRecipeEntry(1, result),
            null, List.of(new ItemRecipeEntry(7, itemA),
            new ItemRecipeEntry(4, itemB)));
    }

    @Override
    protected Collection<CraftingRecipe> createMultipleObjects() {
        Item itemA = itemRepository.insert(universeName,
            itemBuilder.createItemBuilder(universeName).withName("A").buildItem());
        Item itemB = itemRepository.insert(universeName,
            itemBuilder.createItemBuilder(universeName).withName("B").buildItem());

        CraftingRecipe recipeA = new CraftingRecipe(null, "", "", "", new ItemRecipeEntry(1, itemA),
            null, List.of(new ItemRecipeEntry(4, itemB)));
        CraftingRecipe recipeB = new CraftingRecipe(null, "", "", "", new ItemRecipeEntry(1, itemB),
            null, List.of(new ItemRecipeEntry(4, itemA)));

        return List.of(recipeA, recipeB);
    }
}