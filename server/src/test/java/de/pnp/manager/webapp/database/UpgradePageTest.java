package de.pnp.manager.webapp.database;

import de.pnp.manager.component.item.ItemType;
import de.pnp.manager.component.upgrade.Upgrade;
import de.pnp.manager.component.upgrade.effect.AdditiveUpgradeEffect;
import de.pnp.manager.component.upgrade.effect.EUpgradeManipulator;
import de.pnp.manager.component.upgrade.effect.SimpleUpgradeEffect;
import de.pnp.manager.server.TestServer;
import de.pnp.manager.server.configurator.EServerTestConfiguration;
import de.pnp.manager.server.database.item.ItemTypeRepository;
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

    @Autowired
    private ItemTypeRepository typeRepository;

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
            Pair.of("target", Comparator.comparing(upgrade -> upgrade.getTarget().getName())));
    }

    @Override
    protected Upgrade getWrongObject() {
        return new Upgrade(null, null, null, -1, 10, List.of());
    }

    @Override
    protected List<String> getExpectedErrorFields() {
        return List.of("name", "target", "slots");
    }

    @Override
    protected Upgrade getCorrectObject() {
        return new Upgrade(null, "Shine 100", getItemType("Weapon"), 1, 10,
            List.of(new AdditiveUpgradeEffect("+100 Damage", EUpgradeManipulator.DAMAGE, 1),
                new SimpleUpgradeEffect("It shines", EUpgradeManipulator.NONE)));
    }

    @Override
    protected String getIdentifier(Upgrade object) {
        return object.getName();
    }

    @Override
    protected Upgrade getEditedObject() {
        Upgrade original = getOriginalModifiedObject();
        return new Upgrade(null, "Anti Werewolf", original.getTarget(), original.getSlots(), original.getVendorPrice(),
            original.getEffects());
    }

    @Override
    protected String getChangeIdentifier() {
        return "Anti Werewolf";
    }

    @Override
    protected Predicate<Upgrade> getOriginalModifiedFilter() {
        return upgrade -> upgrade.getName().equals("Silver Coating");
    }

    private ItemType getItemType(String name) {
        return typeRepository.get(getUniverseName(), name).orElseThrow();
    }
}
