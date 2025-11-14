package de.pnp.manager.webapp.database;

import de.pnp.manager.component.CraftingRecipe;
import de.pnp.manager.component.IResourceUsage;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.server.TestServer;
import de.pnp.manager.server.configurator.EServerTestConfiguration;
import de.pnp.manager.server.database.CraftingRecipeRepository;
import de.pnp.manager.server.database.MaterialRepository;
import de.pnp.manager.server.database.item.ItemRepository;
import de.pnp.manager.webapp.pages.MainMenu;
import de.pnp.manager.webapp.pages.OverviewBasePage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.function.Predicate;

/**
 * Tests the crafting recipe overview page.
 */
@TestServer(EServerTestConfiguration.BASIC_ITEMS)
public class CraftingRecipePageTest extends RepositoryOverviewTestBase<CraftingRecipe> {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MaterialRepository materialRepository;

    protected CraftingRecipePageTest(@Autowired CraftingRecipeRepository repository) {
        super(repository);
    }

    @Override
    protected OverviewBasePage openTestPage(MainMenu mainMenu) {
        return mainMenu.openCraftingRecipePage();
    }

    @Override
    protected CraftingRecipe getWrongObject() {
        return new CraftingRecipe(null, "", "", "",
                List.of(new IResourceUsage.ItemUsage(-1, null)),
                List.of(new IResourceUsage.ItemUsage(-2, null)));
    }

    @Override
    protected List<String> getExpectedErrorFields() {
        return List.of("products.0.amount", "materials.0.amount");
    }

    @Override
    protected CraftingRecipe getCorrectObject() {
        return new CraftingRecipe(null, "Smith", "NEW", "",
                List.of(new IResourceUsage.ItemUsage(1, getItem("Iron Sword"))),
                List.of(new IResourceUsage.MaterialUsage(1, materialRepository.get(getUniverseId(), "Iron").orElseThrow())));
    }

    @Override
    protected String getIdentifier(CraftingRecipe object) {
        return object.getProducts().getFirst().resource().getName();
    }

    @Override
    protected CraftingRecipe getEditedObject() {
        CraftingRecipe original = getOriginalModifiedObject();
        return new CraftingRecipe(null, original.getProfession(), original.getRequirement(),
                original.getOtherCircumstances(),
                List.of(new IResourceUsage.ItemUsage(1, getItem("Iron Ring"))), original.getMaterials());
    }

    @Override
    protected String getChangeIdentifier() {
        return "Iron Ring";
    }

    @Override
    protected Predicate<CraftingRecipe> getOriginalModifiedFilter() {
        return recipe -> recipe.getProfession().equals("Smith");
    }

    private Item getItem(String name) {
        return itemRepository.get(getUniverseId(), name).orElseThrow();
    }
}
