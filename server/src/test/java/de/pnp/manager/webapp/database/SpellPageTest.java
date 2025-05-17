package de.pnp.manager.webapp.database;

import de.pnp.manager.component.IResourceUsage;
import de.pnp.manager.component.IResourceUsage.CharacterResourceUsage;
import de.pnp.manager.component.spell.Spell;
import de.pnp.manager.server.TestServer;
import de.pnp.manager.server.configurator.EServerTestConfiguration;
import de.pnp.manager.server.database.SpellRepository;
import de.pnp.manager.server.database.TalentRepository;
import de.pnp.manager.server.database.attributes.SecondaryAttributeRepository;
import de.pnp.manager.webapp.pages.MainMenu;
import de.pnp.manager.webapp.pages.OverviewBasePage;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests the spell overview page.
 */
@TestServer(EServerTestConfiguration.CHARACTERS)
public class SpellPageTest extends UniquelyNamedOverviewTestBase<Spell, SpellRepository> {

    @Autowired
    private TalentRepository talentRepository;

    @Autowired
    private SecondaryAttributeRepository secondaryAttributeRepository;

    protected SpellPageTest(@Autowired SpellRepository repository) {
        super(repository);
    }

    @Override
    protected OverviewBasePage openTestPage(MainMenu openMainMenu) {
        return openMainMenu.openSpellPage();
    }

    @Override
    protected String getEditObjectName() {
        return "Fireball";
    }

    @Override
    protected Spell getWrongObject() {
        return null;
    }

    @Override
    protected List<String> getExpectedErrorFields() {
        return List.of("name", "effect", "tier");
    }

    @Override
    protected Spell getCorrectObject() {
        return null;
    }

    @Override
    protected Spell getEditedObject() {
        return null;
    }

    @Override
    protected String getChangeIdentifier() {
        return "D20 Damage";
    }

    private List<IResourceUsage<?>> manaCost(int mana) {
        return List.of(new CharacterResourceUsage(mana,
            secondaryAttributeRepository.get(getUniverseName(), "Mana").orElseThrow()));
    }
}
