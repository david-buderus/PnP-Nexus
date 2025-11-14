package de.pnp.manager.webapp.species;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.AriaRole;
import de.pnp.manager.component.character.Nation;
import de.pnp.manager.component.character.Species;
import de.pnp.manager.component.character.traits.ICharacterTrait;
import de.pnp.manager.server.ServerTestBase;
import de.pnp.manager.server.TestServer;
import de.pnp.manager.server.UiTestServer;
import de.pnp.manager.server.configurator.EServerTestConfiguration;
import de.pnp.manager.server.database.character.NationRepository;
import de.pnp.manager.server.database.character.SpeciesRepository;
import de.pnp.manager.webapp.pages.components.Select;
import de.pnp.manager.webapp.pages.components.species.NationFormPage;
import de.pnp.manager.webapp.pages.components.species.SpeciesFormPage;
import de.pnp.manager.webapp.pages.species.NationDetailPage;
import de.pnp.manager.webapp.pages.species.SpeciesDetailPage;
import de.pnp.manager.webapp.pages.species.SpeciesOverviewPage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
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
        species = speciesRepository.get(getUniverseId(), "Human").orElseThrow();
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
        Assertions.assertThat(speciesRepository.get(getUniverseId(), species.getName()).orElseThrow().getDescription()).contains(newDescription);
    }

    @Test
    void removeNation() {
        page.getEditButton().clickDropdown("removeNation");
        Locator removalDialog = page.asPage().getByRole(AriaRole.DIALOG);
        Select.from(removalDialog.getByTestId("nations-to-remove")).select(species.getNations().getFirst());
        removalDialog.locator("[type=submit]").click();
        assertThat(page.getLink(species.getNations().getFirst())).not().isVisible();

        Assertions.assertThat(speciesRepository.get(getUniverseId(), species.getName()).orElseThrow().getNations()).isEmpty();
    }

    @Test
    void addNation() {
        Nation nation = new Nation(null, "New Nation", "Some text", List.of(), List.of());

        NationFormPage formPage = page.addNation();
        formPage.fillOut(nation);
        formPage.add();

        // Wait until all has been saved
        new NationDetailPage(page.asPage()).assertName(nation.getName());

        // Get the nation with its id
        nation = nationRepository.get(getUniverseId(), nation.getName()).orElseThrow();

        page.toMainMenu().openSpeciesOverviewPage().openSpeciesPage(species);
        assertThat(page.getLink(nation)).isVisible();

        Assertions.assertThat(speciesRepository.get(getUniverseId(), species.getName()).orElseThrow().getNations()).contains(nation);
    }

    @Test
    void addExistingNation() {
        Nation nation = nationRepository.get(getUniverseId(), "The Unbound").orElseThrow();

        page.getEditButton().clickDropdown("addExistingNation");
        Locator addDialog = page.asPage().getByRole(AriaRole.DIALOG);
        Select.from(addDialog.getByTestId("nation-to-add")).select(nation);
        addDialog.locator("[type=submit]").click();
        assertThat(page.getLink(nation)).isVisible();

        Assertions.assertThat(speciesRepository.get(getUniverseId(), species.getName()).orElseThrow().getNations()).contains(nation);
    }
}
