package de.pnp.manager.server.service;

import de.pnp.manager.component.CraftingRecipe;
import de.pnp.manager.component.IResourceUsage.CharacterResourceUsage;
import de.pnp.manager.component.IResourceUsage.ItemUsage;
import de.pnp.manager.component.IResourceUsage.MaterialUsage;
import de.pnp.manager.component.attributes.SecondaryAttribute;
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
        SecondaryAttribute health = createSecondaryAttribute().withName("Health").isConsumable().persist().build();

        return List.of(
            new CraftingRecipe(null, "Smith", "Crafting: 4", "Furnace",
                new ItemUsage(1, ironIngot), null,
                List.of(new ItemUsage(2, createItem().withName("Iron Ore").persist().buildItem()))),
            new CraftingRecipe(null, "", "", "",
                new ItemUsage(1, createItem().withName("Blood").persist().buildItem()), null,
                List.of(new CharacterResourceUsage(10, health))),
            new CraftingRecipe(null, "", "", "",
                new ItemUsage(2, createItem().withName("Steel Ingot").persist().buildItem()),
                new ItemUsage(1, createItem().withName("Slag").persist().buildItem()),
                List.of(new MaterialUsage(2, iron),
                    new ItemUsage(3, createItem().withName("Coal").persist().buildItem())))
        );
    }
}
