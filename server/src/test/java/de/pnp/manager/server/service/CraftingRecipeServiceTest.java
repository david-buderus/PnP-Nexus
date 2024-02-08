package de.pnp.manager.server.service;

import de.pnp.manager.component.CraftingRecipe;
import de.pnp.manager.component.IRecipeEntry.CharacterResourceRecipeEntry;
import de.pnp.manager.component.IRecipeEntry.ECharacterResource;
import de.pnp.manager.component.IRecipeEntry.ItemRecipeEntry;
import de.pnp.manager.component.IRecipeEntry.MaterialRecipeEntry;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.item.Material.MaterialItem;
import de.pnp.manager.server.database.CraftingRecipeRepository;
import de.pnp.manager.server.database.MaterialRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests for {@link CraftingRecipeService}
 */
public class CraftingRecipeServiceTest extends
    RepositoryServiceBaseTest<CraftingRecipe, CraftingRecipeRepository, CraftingRecipeService> {

    @Autowired
    private MaterialRepository materialRepository;

    public CraftingRecipeServiceTest(@Autowired CraftingRecipeService craftingRecipeService,
        @Autowired CraftingRecipeRepository repository) {
        super(craftingRecipeService, repository, CraftingRecipe.class);
    }

    @Override
    protected List<CraftingRecipe> createObjects() {
        Item ironIngot = createItem().withName("Iron Ingot").persist().buildItem();
        Material iron = materialRepository.insert(getUniverseName(), new Material(null, "Iron",
            List.of(new MaterialItem(1, ironIngot))));

        return List.of(
            new CraftingRecipe(null, "Smith", "Crafting: 4", "Furnace",
                new ItemRecipeEntry(1, ironIngot), null,
                List.of(new ItemRecipeEntry(2, createItem().withName("Iron Ore").persist().buildItem()))),
            new CraftingRecipe(null, "", "", "",
                new ItemRecipeEntry(1, createItem().withName("Blood").persist().buildItem()), null,
                List.of(new CharacterResourceRecipeEntry(10, ECharacterResource.HEALTH))),
            new CraftingRecipe(null, "", "", "",
                new ItemRecipeEntry(2, createItem().withName("Steel Ingot").persist().buildItem()),
                new ItemRecipeEntry(1, createItem().withName("Slag").persist().buildItem()),
                List.of(new MaterialRecipeEntry(2, iron),
                    new ItemRecipeEntry(3, createItem().withName("Coal").persist().buildItem())))
        );
    }
}
