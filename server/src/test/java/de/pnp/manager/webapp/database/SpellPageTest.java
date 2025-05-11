package de.pnp.manager.webapp.database;

import de.pnp.manager.component.IResourceUsage;
import de.pnp.manager.component.IResourceUsage.CharacterResourceUsage;
import de.pnp.manager.component.Spell;
import de.pnp.manager.component.character.Talent;
import de.pnp.manager.server.TestServer;
import de.pnp.manager.server.configurator.EServerTestConfiguration;
import de.pnp.manager.server.database.SpellRepository;
import de.pnp.manager.server.database.TalentRepository;
import de.pnp.manager.server.database.attributes.SecondaryAttributeRepository;
import de.pnp.manager.webapp.pages.MainMenu;
import de.pnp.manager.webapp.pages.OverviewBasePage;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;
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
    protected List<Pair<String, Comparator<Spell>>> getSorters() {
        return List.of(Pair.of("effect", Comparator.comparing(Spell::getEffect)),
            Pair.of("talents", Comparator.comparing(
                spell -> spell.getTalents().stream().map(Talent::getName).collect(Collectors.joining(", ")))));
    }

    @Override
    protected Spell getWrongObject() {
        return new Spell(null, "", "", null, "", "",
            talentRepository.getByName(getUniverseName(), "Fire Magic").stream().toList(), -1, Set.of());
    }

    @Override
    protected List<String> getExpectedErrorFields() {
        return List.of("name", "effect", "tier");
    }

    @Override
    protected Spell getCorrectObject() {
        return new Spell(null, "Burn", "Burns the target", manaCost(3), "", "",
            talentRepository.getByName(getUniverseName(), "Fire Magic").stream().toList(), 1, Set.of());
    }

    @Override
    protected Spell getEditedObject() {
        return new Spell(null, "Big Fireball", "D20 Damage", manaCost(20), "", "",
            getOriginalModifyObject().getTalents(), 1, Set.of());
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
