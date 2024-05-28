package de.pnp.manager.webapp.database;

import de.pnp.manager.component.CraftingRecipe;
import de.pnp.manager.component.IRecipeEntry.ItemRecipeEntry;
import de.pnp.manager.component.IRecipeEntry.MaterialRecipeEntry;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.server.TestServer;
import de.pnp.manager.server.configurator.EServerTestConfiguration;
import de.pnp.manager.server.database.CraftingRecipeRepository;
import de.pnp.manager.server.database.MaterialRepository;
import de.pnp.manager.server.database.item.ItemRepository;
import de.pnp.manager.webapp.pages.MainMenu;
import de.pnp.manager.webapp.pages.OverviewBasePage;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import org.apache.commons.lang3.tuple.Pair;
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
    protected Comparator<CraftingRecipe> getDefaultSort() {
        return Comparator.comparing(recipe -> recipe.getProduct().resource().getName());
    }

    @Override
    protected List<Pair<String, Comparator<CraftingRecipe>>> getSorters() {
        return List.of(Pair.of("profession", Comparator.comparing(CraftingRecipe::getProfession)),
            Pair.of("requirement", Comparator.comparing(CraftingRecipe::getRequirement)));
    }

    @Override
    protected CraftingRecipe getWrongObject() {
        return new CraftingRecipe(null, "", "", "", new ItemRecipeEntry(-1, null), null, List.of());
    }

    @Override
    protected List<String> getExpectedErrorFields() {
        return List.of("product.amount", "product.resource");
    }

    @Override
    protected CraftingRecipe getCorrectObject() {
        return new CraftingRecipe(null, "Smith", "Forge", "", new ItemRecipeEntry(1, getItem("Iron Sword")), null,
            List.of(new MaterialRecipeEntry(1, materialRepository.get(getUniverseName(), "Iron").orElseThrow()),
                new ItemRecipeEntry(1, getItem("Iron Ingot"))));
    }

    @Override
    protected String getIdentifier(CraftingRecipe object) {
        return object.getProduct().resource().getName();
    }

    @Override
    protected CraftingRecipe getEditedObject() {
        CraftingRecipe original = getOriginalModifiedObject();
        return new CraftingRecipe(null, "Smith", "Example", "Something", original.getProduct(),
            original.getSideProduct(), List.of(new ItemRecipeEntry(1, getItem("Iron Ring"))));
    }

    @Override
    protected String getChangeIdentifier() {
        return "Iron Ring";
    }

    @Override
    protected Predicate<CraftingRecipe> getOriginalModifiedFilter() {
        return recipe -> recipe.getProduct().resource().getName().equals("Iron Ingot");
    }

    private Item getItem(String name) {
        return itemRepository.get(getUniverseName(), name).orElseThrow();
    }
}
