package de.pnp.manager.webapp.database;

import de.pnp.manager.component.IResourceUsage.ItemUsage;
import de.pnp.manager.component.upgrade.Upgrade;
import de.pnp.manager.component.upgrade.UpgradeRecipe;
import de.pnp.manager.server.TestServer;
import de.pnp.manager.server.configurator.EServerTestConfiguration;
import de.pnp.manager.server.database.RepositoryBase;
import de.pnp.manager.server.database.item.ItemRepository;
import de.pnp.manager.server.database.upgrade.UpgradeRepository;
import de.pnp.manager.webapp.pages.MainMenu;
import de.pnp.manager.webapp.pages.OverviewBasePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.function.Predicate;

/**
 * Tests the upgrade recipe overview page.
 */
@TestServer(EServerTestConfiguration.BASIC_ITEMS)
public class UpgradeRecipePageTest extends RepositoryOverviewTestBase<UpgradeRecipe> {

    @Autowired
    private UpgradeRepository upgradeRepository;

    @Autowired
    private ItemRepository itemRepository;

    protected UpgradeRecipePageTest(@Autowired RepositoryBase<UpgradeRecipe> repository) {
        super(repository);
    }

    @Override
    protected OverviewBasePage openTestPage(MainMenu mainMenu) {
        return mainMenu.openUpgradeRecipePage();
    }

    @Override
    protected UpgradeRecipe getWrongObject() {
        return new UpgradeRecipe(null, null, List.of(), "", List.of(new ItemUsage(-1, null)));
    }

    @Override
    protected List<String> getExpectedErrorFields() {
        return List.of("upgrade");
    }

    @Override
    protected UpgradeRecipe getCorrectObject() {
        return new UpgradeRecipe(null, getUpgrade("Fire 1"), List.of(), "",
                List.of(new ItemUsage(2, itemRepository.get(getUniverseName(), "Coal").orElseThrow())));
    }

    @Override
    protected String getIdentifier(UpgradeRecipe object) {
        return object.getUpgrade().getName();
    }

    @Override
    protected UpgradeRecipe getEditedObject() {
        UpgradeRecipe original = getOriginalModifiedObject();
        return new UpgradeRecipe(null, original.getUpgrade(), original.getRequiredUpgrades(), "CHANGE",
                original.getMaterials());
    }

    @Override
    protected String getChangeIdentifier() {
        return "CHANGE";
    }

    @Override
    protected Predicate<UpgradeRecipe> getOriginalModifiedFilter() {
        return upgradeRecipe -> upgradeRecipe.getUpgrade().getName().equals("Sharpness 2");
    }

    private Upgrade getUpgrade(String name) {
        return upgradeRepository.get(getUniverseName(), Query.query(Criteria.where("name").is(name))).orElseThrow();
    }
}
