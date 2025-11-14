package de.pnp.manager.webapp.database;

import de.pnp.manager.component.IResourceUsage;
import de.pnp.manager.component.IResourceUsage.CharacterResourceUsage;
import de.pnp.manager.component.character.Talent;
import de.pnp.manager.component.spell.Spell;
import de.pnp.manager.server.TestServer;
import de.pnp.manager.server.configurator.EServerTestConfiguration;
import de.pnp.manager.server.database.SpellRepository;
import de.pnp.manager.server.database.TalentRepository;
import de.pnp.manager.server.database.attributes.SecondaryAttributeRepository;
import de.pnp.manager.webapp.pages.MainMenu;
import de.pnp.manager.webapp.pages.OverviewBasePage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static de.pnp.manager.utils.TestSpellBuilder.createSpellBuilder;

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
    protected Spell getWrongObject() {
        return createSpellBuilder().withName("").withEffect("").withTier(-1).build();
    }

    @Override
    protected List<String> getExpectedErrorFields() {
        return List.of("name", "effect", "tier");
    }

    @Override
    protected Spell getCorrectObject() {
        return createSpellBuilder().withName("Fly").withEffect("Caster can fly for some time").withTier(4)
                .withCost(manaCost(20)).withTalents(talentRepository.getByName(getUniverseId(), "Casting")
                        .toArray(new Talent[0]))
                .withTags("Flying").build();
    }

    @Override
    protected String getEditObjectName() {
        return "Fireball";
    }

    @Override
    protected Spell getEditedObject() {
        Spell spell = getOriginalModifyObject();
        return new Spell(null, spell.getName(), "D20 Damage", manaCost(15), spell.getAdditionalCost(),
                spell.getCastTime(), spell.getCooldown(), spell.getAction(), spell.getCast(), spell.getCastingTypes(),
                spell.getTier(), spell.getTags(), spell.getCountermeasures());
    }

    @Override
    protected String getChangeIdentifier() {
        return "D20 Damage";
    }

    private List<IResourceUsage<?>> manaCost(int mana) {
        return List.of(new CharacterResourceUsage(mana,
                secondaryAttributeRepository.get(getUniverseId(), "Mana").orElseThrow()));
    }
}
