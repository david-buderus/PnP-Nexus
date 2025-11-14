package de.pnp.manager.server.database;

import de.pnp.manager.component.attributes.PrimaryAttribute;
import de.pnp.manager.component.attributes.SecondaryAttribute;
import de.pnp.manager.component.character.Talent;
import de.pnp.manager.component.spell.Spell;
import de.pnp.manager.server.database.attributes.PrimaryAttributeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static de.pnp.manager.utils.TestUtils.tagSet;

/**
 * Tests for {@link SpellRepository}.
 */
public class SpellRepositoryTest extends RepositoryTestBase<Spell, SpellRepository> {

    @Autowired
    private TalentRepository talentRepository;

    @Autowired
    private PrimaryAttributeRepository primaryAttributeRepository;

    public SpellRepositoryTest(@Autowired SpellRepository repository) {
        super(repository);
    }

    @Test
    void testTalentLink() {
        PrimaryAttribute primaryAttribute = primaryAttributeRepository.insert(getUniverseId(),
                new PrimaryAttribute(null, "Primary", "PRI"));

        Talent earthMagic = talentRepository.insert(getUniverseId(),
                new Talent(null, "Earth Magic", tagSet("Magic"), primaryAttribute, primaryAttribute, primaryAttribute));
        Spell spell = createSpell().withName("Wall").withEffect("Create a Wall").withAdditionalCost("10 Mana per meter")
                .withTalents(earthMagic).build();
        Talent changedEarthMagic = new Talent(null, "Earth Magic", tagSet("Magic"), primaryAttribute, primaryAttribute,
                primaryAttribute);

        testRepositoryCollectionLink(s -> ((Spell.TalentCast) s.getCast()).talents(), talentRepository, spell, List.of(earthMagic),
                Map.of(earthMagic, changedEarthMagic));
    }

    @Override
    protected Spell createObject() {
        PrimaryAttribute primaryAttribute = primaryAttributeRepository.insert(getUniverseId(),
                new PrimaryAttribute(null, "Primary", "PRI"));
        SecondaryAttribute mana = createSecondaryAttribute().withName("Mana").isConsumable().persist().build();
        Talent fireMagic = talentRepository.insert(getUniverseId(),
                new Talent(null, "Fire Magic", tagSet("Magic"), primaryAttribute, primaryAttribute, primaryAttribute));

        return createSpell().withName("Fireball").withEffect("Throw a fireball").withCost(10, mana)
                .withTalents(fireMagic).withTier(1).build();
    }

    @Override
    protected Spell createSlightlyChangeObject() {
        PrimaryAttribute primaryAttribute = primaryAttributeRepository.insert(getUniverseId(),
                new PrimaryAttribute(null, "Other", "OT"));
        SecondaryAttribute life = createSecondaryAttribute().withName("Life").isConsumable().persist().build();
        Talent fireMagic = talentRepository.insert(getUniverseId(),
                new Talent(null, "Fire Magic", tagSet("Magic"), primaryAttribute, primaryAttribute, primaryAttribute));

        return createSpell().withName("Big Fireball").withEffect("Throw a fireball").withCost(10, life)
                .withTalents(fireMagic).withTier(3).build();
    }

    @Override
    protected List<Spell> createMultipleObjects() {
        PrimaryAttribute primaryAttribute = primaryAttributeRepository.insert(getUniverseId(),
                new PrimaryAttribute(null, "Primary", "PRI"));
        Talent earthMagic = talentRepository.insert(getUniverseId(),
                new Talent(null, "Earth Magic", tagSet("Magic"), primaryAttribute, primaryAttribute, primaryAttribute));

        return List.of(
                createSpell().withName("Wall").withEffect("Creates a wall").withTalents(earthMagic).withTier(2).build(),
                createSpell().withName("Stone").withEffect("Throws a stone").withTalents(earthMagic).withTier(1).build()
        );
    }
}
