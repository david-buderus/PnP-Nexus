package de.pnp.manager.webapp.species;

import de.pnp.manager.component.character.Nation;
import de.pnp.manager.component.character.Species;
import de.pnp.manager.component.character.traits.ICharacterTrait;
import de.pnp.manager.server.ServerTestBase;
import de.pnp.manager.server.TestServer;
import de.pnp.manager.server.UiTestServer;
import de.pnp.manager.server.configurator.EServerTestConfiguration;
import de.pnp.manager.server.database.character.NationRepository;
import de.pnp.manager.server.database.character.SpeciesRepository;
import de.pnp.manager.webapp.pages.components.species.NationFormPage;
import de.pnp.manager.webapp.pages.species.NationDetailPage;
import de.pnp.manager.webapp.pages.species.SpeciesOverviewPage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests the species detail page.
 */
@TestServer(EServerTestConfiguration.SPECIES)
@UiTestServer
public class NationDetailPageTest extends ServerTestBase {

    @Autowired
    private SpeciesRepository speciesRepository;

    @Autowired
    private NationRepository nationRepository;

    private NationDetailPage page;

    private Nation nation;

    @BeforeEach
    void openPage() {
        SpeciesOverviewPage overviewPage = webDriver.openMainMenu("admin", "admin").openSpeciesOverviewPage();
        overviewPage.selectActiveUniverse(getUniverse());
        Species species = speciesRepository.get(getUniverseName(), "Human").orElseThrow();
        nation = species.getNations().getFirst();
        page = overviewPage.openNationPage(species, nation);
    }

    @Test
    void content() {
        page.assertName(nation.getName());
        page.assertDescription(nation.getDescription());

        for (ICharacterTrait trait : nation.getAdvantageTraits()) {
            page.assertAdvantageTrait(trait);
        }
        for (ICharacterTrait trait : nation.getDisadvantageTraits()) {
            page.assertDisadvantageTrait(trait);
        }
    }

    @Test
    void edit() {
        NationFormPage form = page.edit();
        String newDescription = "New description";
        Nation editedNation = new Nation(null, nation.getName(), newDescription, nation.getAdvantageTraits(), nation.getDisadvantageTraits());
        form.fillOut(editedNation);
        form.edit();

        page.assertDescription(newDescription);
        Assertions.assertThat(nationRepository.get(getUniverseName(), nation.getName()).orElseThrow().getDescription()).contains(newDescription);
    }
}
