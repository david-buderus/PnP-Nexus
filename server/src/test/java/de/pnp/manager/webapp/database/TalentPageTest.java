package de.pnp.manager.webapp.database;

import de.pnp.manager.component.attributes.PrimaryAttribute;
import de.pnp.manager.component.character.Talent;
import de.pnp.manager.server.TestServer;
import de.pnp.manager.server.configurator.EServerTestConfiguration;
import de.pnp.manager.server.database.TalentRepository;
import de.pnp.manager.server.database.attributes.PrimaryAttributeRepository;
import de.pnp.manager.webapp.pages.MainMenu;
import de.pnp.manager.webapp.pages.OverviewBasePage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.function.Predicate;

import static de.pnp.manager.utils.TestUtils.tagSet;

/**
 * Tests the talent overview page.
 */
@TestServer(EServerTestConfiguration.CHARACTERS)
public class TalentPageTest extends RepositoryOverviewTestBase<Talent> {

    @Autowired
    private PrimaryAttributeRepository primaryAttributeRepository;

    protected TalentPageTest(@Autowired TalentRepository repository) {
        super(repository);
    }

    @Override
    protected OverviewBasePage openTestPage(MainMenu mainMenu) {
        return mainMenu.openTalentPage();
    }

    @Override
    protected Talent getWrongObject() {
        return new Talent(null, "", tagSet(), getExampleAttribute(),
                null, null);
    }

    @Override
    protected List<String> getExpectedErrorFields() {
        return List.of("name", "secondAttribute", "thirdAttribute");
    }

    @Override
    protected Talent getCorrectObject() {
        return new Talent(null, "Body Strength", tagSet("Body"), getExampleAttribute(), getExampleAttribute(),
                getExampleAttribute());
    }

    @Override
    protected String getIdentifier(Talent object) {
        return "Body Strength";
    }

    @Override
    protected Talent getEditedObject() {
        return new Talent(null, "Real Casting", tagSet("Magic"), getOriginalModifiedObject().getFirstAttribute(),
                getOriginalModifiedObject().getSecondAttribute(), getOriginalModifiedObject().getThirdAttribute());
    }

    @Override
    protected String getChangeIdentifier() {
        return "Real Casting";
    }

    private PrimaryAttribute getExampleAttribute() {
        return primaryAttributeRepository.get(getUniverseName(), "Strength").orElseThrow();
    }

    @Override
    protected Predicate<Talent> getOriginalModifiedFilter() {
        return talent -> "Casting".equals(talent.getName());
    }
}
