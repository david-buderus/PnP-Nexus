package de.pnp.manager.webapp.species;

import com.microsoft.playwright.Locator;
import de.pnp.manager.component.character.Nation;
import de.pnp.manager.component.character.Species;
import de.pnp.manager.server.ServerTestBase;
import de.pnp.manager.server.TestServer;
import de.pnp.manager.server.UiTestServer;
import de.pnp.manager.server.configurator.EServerTestConfiguration;
import de.pnp.manager.server.database.character.NationRepository;
import de.pnp.manager.server.database.character.SpeciesRepository;
import de.pnp.manager.webapp.pages.SpeciesOverviewPage;
import de.pnp.manager.webapp.pages.components.species.SpeciesFormPage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * Tests the species overview page.
 */
@TestServer(EServerTestConfiguration.SPECIES)
@UiTestServer
public class SpeciesOverviewPageTest extends ServerTestBase {

    @Autowired
    private SpeciesRepository speciesRepository;

    @Autowired
    private NationRepository nationRepository;

    private SpeciesOverviewPage page;

    @BeforeEach
    void openPage() {
        page = webDriver.openMainMenu("admin", "admin").openSpeciesOverviewPage();
        page.selectActiveUniverse(getUniverse());
    }

    @Test
    void links() {
        Collection<Species> species = speciesRepository.getAll(getUniverseName());
        Collection<Nation> nations = nationRepository.getAll(getUniverseName());
        List<Nation> unboundNations = nations.stream().filter(n -> species.stream().noneMatch(s -> s.getNations().contains(n))).toList();

        for (Species s : species) {
            Locator speciesLink = page.getLink(s);
            assertThat(speciesLink).isVisible();
            speciesLink.click();
            assertThat(page.asPage()).hasURL(Pattern.compile("http://localhost:\\d+/species/" + s.getId() + "\\?.*"));
            page.toMainMenu().openSpeciesOverviewPage();

            for (Nation nation : s.getNations()) {
                Locator nationLink = page.getLink(s, nation);
                assertThat(nationLink).isVisible();
                nationLink.click();
                assertThat(page.asPage()).hasURL(Pattern.compile("http://localhost:\\d+/nations/" + nation.getId() + "\\?.*"));
                page.toMainMenu().openSpeciesOverviewPage();
            }
        }
        for (Nation nation : unboundNations) {
            Locator nationLink = page.getLink(nation);
            assertThat(nationLink).isVisible();
            nationLink.click();
            assertThat(page.asPage()).hasURL(Pattern.compile("http://localhost:\\d+/nations/" + nation.getId() + "\\?.*"));
            page.toMainMenu().openSpeciesOverviewPage();
        }
    }

    @Test
    void add() {
        SpeciesFormPage form = page.openSpeciesFormPage();
        Species species = new Species(null, "Test", "Some test description", true, List.of(), List.of(), List.of());
        form.fillOut(species);
        form.add();

        assertThat(page.asPage().getByTestId("name")).hasText(species.getName());
        Assertions.assertThat(speciesRepository.getAll(getUniverseName()))
                .contains(new Species(
                        null,
                        species.getName(),
                        "<p>" + species.getDescription() + "</p>", // The description gets stored as HTML
                        species.isPlayable(),
                        species.getAdvantageTraits(),
                        species.getDisadvantageTraits(),
                        species.getNations()
                ));
    }
}
