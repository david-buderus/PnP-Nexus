package de.pnp.manager.webapp.database;

import de.pnp.manager.component.attributes.SecondaryAttribute;
import de.pnp.manager.component.attributes.SecondaryAttribute.PrimaryAttributeDependency;
import de.pnp.manager.server.TestServer;
import de.pnp.manager.server.configurator.EServerTestConfiguration;
import de.pnp.manager.server.database.attributes.PrimaryAttributeRepository;
import de.pnp.manager.server.database.attributes.SecondaryAttributeRepository;
import de.pnp.manager.webapp.pages.MainMenu;
import de.pnp.manager.webapp.pages.OverviewBasePage;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests the secondary attribute overview page.
 */
@TestServer(EServerTestConfiguration.CHARACTERS)
public class SecondaryAttributePageTest extends
    UniquelyNamedOverviewTestBase<SecondaryAttribute, SecondaryAttributeRepository> {

    @Autowired
    private PrimaryAttributeRepository primaryAttributeRepository;

    public SecondaryAttributePageTest(@Autowired SecondaryAttributeRepository repository) {
        super(repository);
    }

    @Override
    protected OverviewBasePage openTestPage(MainMenu mainMenu) {
        return mainMenu.openSecondaryAttributePage();
    }

    @Override
    protected List<Pair<String, Comparator<SecondaryAttribute>>> getSorters() {
        return List.of();
    }

    @Override
    protected SecondaryAttribute getWrongObject() {
        return new SecondaryAttribute(null, "", false, List.of(
            new PrimaryAttributeDependency(0.5, null)));
    }

    @Override
    protected List<String> getExpectedErrorFields() {
        return List.of("name", "primaryAttributeDependencies[0].primaryAttribute");
    }

    @Override
    protected SecondaryAttribute getCorrectObject() {
        return new SecondaryAttribute(null, "Melee Damage", false, List.of(
            new PrimaryAttributeDependency(0.5,
                primaryAttributeRepository.get(getUniverseName(), "Strength").orElseThrow())));
    }

    @Override
    protected SecondaryAttribute getEditedObject() {
        return new SecondaryAttribute(null, "Mana Points", true,
            getOriginalModifyObject().getPrimaryAttributeDependencies());
    }

    @Override
    protected String getChangeIdentifier() {
        return "Mana Points";
    }

    @Override
    protected String getEditObjectName() {
        return "Mana";
    }
}
