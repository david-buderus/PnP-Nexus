package de.pnp.manager.webapp.database;

import de.pnp.manager.component.attributes.PrimaryAttribute;
import de.pnp.manager.component.character.Talent;
import de.pnp.manager.server.TestServer;
import de.pnp.manager.server.configurator.EServerTestConfiguration;
import de.pnp.manager.server.database.TalentRepository;
import de.pnp.manager.server.database.attributes.PrimaryAttributeRepository;
import de.pnp.manager.webapp.pages.MainMenu;
import de.pnp.manager.webapp.pages.OverviewBasePage;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests the talent overview page.
 */
@TestServer(EServerTestConfiguration.CHARACTERS)
@ResourceLock("Characters")
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
    protected Comparator<Talent> getDefaultSort() {
        return Comparator.comparing(Talent::getName);
    }

    @Override
    protected List<Pair<String, Comparator<Talent>>> getSorters() {
        return List.of(Pair.of("group", Comparator.comparing(Talent::getGroup)),
            Pair.of("firstAttribute", Comparator.comparing(talent -> talent.getFirstAttribute().getName())));
    }

    @Override
    protected Talent getWrongObject() {
        return new Talent(null, "", "", getExampleAttribute(),
            null, null);
    }

    @Override
    protected List<String> getExpectedErrorFields() {
        return List.of("name", "group", "secondAttribute", "thirdAttribute");
    }

    @Override
    protected Talent getCorrectObject() {
        return new Talent(null, "Body Strength", "Body", getExampleAttribute(), getExampleAttribute(),
            getExampleAttribute());
    }

    @Override
    protected String getIdentifier(Talent object) {
        return "Body Strength";
    }

    @Override
    protected Talent getEditedObject() {
        return new Talent(null, "Real Fire Magic", "Magic", getOriginalModifiedObject().getFirstAttribute(),
            getOriginalModifiedObject().getSecondAttribute(), getOriginalModifiedObject().getThirdAttribute());
    }

    @Override
    protected String getChangeIdentifier() {
        return "Real Fire Magic";
    }

    private PrimaryAttribute getExampleAttribute() {
        return primaryAttributeRepository.get(getUniverseName(), "Strength").orElseThrow();
    }

    @Override
    protected Predicate<Talent> getOriginalModifiedFilter() {
        return talent -> "Fire Magic".equals(talent.getName());
    }
}
