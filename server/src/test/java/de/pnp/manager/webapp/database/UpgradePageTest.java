package de.pnp.manager.webapp.database;

import de.pnp.manager.component.ECalculation;
import de.pnp.manager.component.TagRequirement;
import de.pnp.manager.component.upgrade.EUpgradeRestriction;
import de.pnp.manager.component.upgrade.Upgrade;
import de.pnp.manager.component.upgrade.effect.EItemEquipmentManipulator;
import de.pnp.manager.component.upgrade.effect.EquipmentItemEffect;
import de.pnp.manager.component.upgrade.effect.SimpleItemEffect;
import de.pnp.manager.server.TestServer;
import de.pnp.manager.server.configurator.EServerTestConfiguration;
import de.pnp.manager.server.database.upgrade.UpgradeRepository;
import de.pnp.manager.webapp.pages.MainMenu;
import de.pnp.manager.webapp.pages.OverviewBasePage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.function.Predicate;

/**
 * Tests the upgrade overview page.
 */
@TestServer(EServerTestConfiguration.BASIC_ITEMS)
public class UpgradePageTest extends RepositoryOverviewTestBase<Upgrade> {

    protected UpgradePageTest(@Autowired UpgradeRepository repository) {
        super(repository);
    }

    @Override
    protected OverviewBasePage openTestPage(MainMenu mainMenu) {
        return mainMenu.openUpgradePage();
    }

    @Override
    protected Upgrade getWrongObject() {
        return new Upgrade(null, null, EUpgradeRestriction.ITEM, TagRequirement.NO_REQUIREMENT, -1, 10, List.of());
    }

    @Override
    protected List<String> getExpectedErrorFields() {
        return List.of("name", "slots");
    }

    @Override
    protected Upgrade getCorrectObject() {
        return new Upgrade(null, "Shine 100", EUpgradeRestriction.WEAPON, TagRequirement.NO_REQUIREMENT, 1, 10,
                List.of(new EquipmentItemEffect("+100 Damage", 1, EItemEquipmentManipulator.DAMAGE,
                                ECalculation.ADDITIVE),
                        new SimpleItemEffect("It shines")));
    }

    @Override
    protected String getIdentifier(Upgrade object) {
        return object.getName();
    }

    @Override
    protected Upgrade getEditedObject() {
        Upgrade original = getOriginalModifiedObject();
        return new Upgrade(null, "Anti Werewolf", original.getRestriction(), original.getTagRequirement(),
                original.getSlots(), original.getVendorPrice(), original.getEffects());
    }

    @Override
    protected String getChangeIdentifier() {
        return "Anti Werewolf";
    }

    @Override
    protected Predicate<Upgrade> getOriginalModifiedFilter() {
        return upgrade -> upgrade.getName().equals("Sharpness 2");
    }
}
