package de.pnp.manager.webapp.species;

import com.microsoft.playwright.Locator;
import de.pnp.manager.component.character.Nation;
import de.pnp.manager.component.character.Species;
import de.pnp.manager.component.character.traits.ICharacterTrait;
import de.pnp.manager.server.ServerTestBase;
import de.pnp.manager.server.TestServer;
import de.pnp.manager.server.UiTestServer;
import de.pnp.manager.server.configurator.EServerTestConfiguration;
import de.pnp.manager.server.database.character.NationRepository;
import de.pnp.manager.server.database.character.SpeciesRepository;
import de.pnp.manager.webapp.pages.components.species.SpeciesFormPage;
import de.pnp.manager.webapp.pages.species.SpeciesDetailPage;
import de.pnp.manager.webapp.pages.species.SpeciesOverviewPage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * Tests the species detail page.
 */
@TestServer(EServerTestConfiguration.SPECIES)
@UiTestServer
public class SpeciesDetailPageTest extends ServerTestBase {

    @Autowired
    private SpeciesRepository speciesRepository;

    @Autowired
    private NationRepository nationRepository;

    private SpeciesDetailPage page;

    private Species species;

    @BeforeEach
    void openPage() {
        SpeciesOverviewPage overviewPage = webDriver.openMainMenu("admin", "admin").openSpeciesOverviewPage();
        overviewPage.selectActiveUniverse(getUniverse());
        species = speciesRepository.get(getUniverseName(), "Human").orElseThrow();
        page = overviewPage.openSpeciesPage(species);
    }

    @Test
    void content() {
        page.assertName(species.getName());
        page.assertDescription(species.getDescription());

        for (ICharacterTrait trait : species.getAdvantageTraits()) {
            page.assertAdvantageTrait(trait);
        }
        for (ICharacterTrait trait : species.getDisadvantageTraits()) {
            page.assertDisadvantageTrait(trait);
        }

        for (Nation nation : species.getNations()) {
            Locator nationLink = page.getLink(nation);
            assertThat(nationLink).isVisible();
            nationLink.click();
            assertThat(page.asPage()).hasURL(Pattern.compile("http://localhost:\\d+/nations/" + nation.getId() + "\\?.*"));
            page.toMainMenu().openSpeciesOverviewPage().openSpeciesPage(species);
        }
    }

    @Test
    void edit() {
        SpeciesFormPage form = page.edit();
        String newDescription = "New description";
        Species editedSpecies = new Species(null, species.getName(), newDescription, species.isPlayable(), species.getAdvantageTraits(), species.getDisadvantageTraits(), species.getNations());
        form.fillOut(editedSpecies);
        form.edit();

        page.assertDescription(newDescription);
        Assertions.assertThat(speciesRepository.get(getUniverseName(), species.getName()).orElseThrow().getDescription()).contains(newDescription);
    }
}
