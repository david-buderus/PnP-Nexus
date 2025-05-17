package de.pnp.manager.webapp.database;

import de.pnp.manager.component.CraftingRecipe;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.server.TestServer;
import de.pnp.manager.server.configurator.EServerTestConfiguration;
import de.pnp.manager.server.database.CraftingRecipeRepository;
import de.pnp.manager.server.database.MaterialRepository;
import de.pnp.manager.server.database.item.ItemRepository;
import de.pnp.manager.webapp.pages.MainMenu;
import de.pnp.manager.webapp.pages.OverviewBasePage;
import java.util.List;
import java.util.function.Predicate;
import org.springframework.beans.factory.annotation.Autowired;

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
        return null;
    }

    @Override
    protected List<String> getExpectedErrorFields() {
        return List.of("product.amount", "product.resource");
    }

    @Override
    protected CraftingRecipe getCorrectObject() {
        return null;
    }

    @Override
    protected String getIdentifier(CraftingRecipe object) {
        return null;
    }

    @Override
    protected CraftingRecipe getEditedObject() {
        CraftingRecipe original = getOriginalModifiedObject();
        return null;
    }

    @Override
    protected String getChangeIdentifier() {
        return "Iron Ring";
    }

    @Override
    protected Predicate<CraftingRecipe> getOriginalModifiedFilter() {
        return null;
    }

    private Item getItem(String name) {
        return itemRepository.get(getUniverseName(), name).orElseThrow();
    }
}
