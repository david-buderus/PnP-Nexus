package de.pnp.manager.webapp.database;

import de.pnp.manager.component.attributes.PrimaryAttribute;
import de.pnp.manager.server.TestServer;
import de.pnp.manager.server.configurator.EServerTestConfiguration;
import de.pnp.manager.server.database.attributes.PrimaryAttributeRepository;
import de.pnp.manager.webapp.pages.MainMenu;
import de.pnp.manager.webapp.pages.OverviewBasePage;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests the primary attribute overview page.
 */
@TestServer(EServerTestConfiguration.CHARACTERS)
public class PrimaryAttributePageTest extends
    UniquelyNamedOverviewTestBase<PrimaryAttribute, PrimaryAttributeRepository> {

    public PrimaryAttributePageTest(@Autowired PrimaryAttributeRepository repository) {
        super(repository);
    }

    @Override
    protected OverviewBasePage openTestPage(MainMenu mainMenu) {
        return mainMenu.openPrimaryAttributePage();
    }

    @Override
    protected List<Pair<String, Comparator<PrimaryAttribute>>> getSorters() {
        return List.of(Pair.of("shortName", Comparator.comparing(PrimaryAttribute::getShortName)));
    }

    @Override
    protected PrimaryAttribute getWrongObject() {
        return new PrimaryAttribute(null, "Example Attribute", "");
    }

    @Override
    protected List<String> getExpectedErrorFields() {
        return List.of("shortName");
    }

    @Override
    protected PrimaryAttribute getCorrectObject() {
        return new PrimaryAttribute(null, "Example Attribute", "EA");
    }

    @Override
    protected PrimaryAttribute getEditedObject() {
        return new PrimaryAttribute(null, "Strength", "STG");
    }

    @Override
    protected String getChangeIdentifier() {
        return "STG";
    }

    @Override
    protected String getEditObjectName() {
        return "Strength";
    }
}
