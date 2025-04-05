package de.pnp.manager.webapp.database;

import de.pnp.manager.component.ECalculation;
import de.pnp.manager.component.TagRequirement;
import de.pnp.manager.component.upgrade.EUpgradeRestriction;
import de.pnp.manager.component.upgrade.Upgrade;
import de.pnp.manager.component.upgrade.effect.EUpgradeEquipmentManipulator;
import de.pnp.manager.component.upgrade.effect.EquipmentUpgradeEffect;
import de.pnp.manager.component.upgrade.effect.SimpleUpgradeEffect;
import de.pnp.manager.server.TestServer;
import de.pnp.manager.server.configurator.EServerTestConfiguration;
import de.pnp.manager.server.database.upgrade.UpgradeRepository;
import de.pnp.manager.webapp.pages.MainMenu;
import de.pnp.manager.webapp.pages.OverviewBasePage;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;

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
    protected Comparator<Upgrade> getDefaultSort() {
        return Comparator.comparing(Upgrade::getName);
    }

    @Override
    protected List<Pair<String, Comparator<Upgrade>>> getSorters() {
        return List.of(Pair.of("slots", Comparator.comparing(Upgrade::getSlots)),
            Pair.of("restriction", Comparator.comparing(Upgrade::getRestriction)));
    }

    @Override
    protected Upgrade getWrongObject() {
        return new Upgrade(null, null, EUpgradeRestriction.ITEM, TagRequirement.NO_REQUIREMENT, -1, 10, List.of());
    }

    @Override
    protected List<String> getExpectedErrorFields() {
        return List.of("name", "target", "slots");
    }

    @Override
    protected Upgrade getCorrectObject() {
        return new Upgrade(null, "Shine 100", EUpgradeRestriction.WEAPON, TagRequirement.NO_REQUIREMENT, 1, 10,
            List.of(new EquipmentUpgradeEffect("+100 Damage", 1, EUpgradeEquipmentManipulator.DAMAGE,
                    ECalculation.ADDITIVE),
                new SimpleUpgradeEffect("It shines")));
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
        return upgrade -> upgrade.getName().equals("Silver Coating");
    }
}
