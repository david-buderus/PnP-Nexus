package de.pnp.manager.server.database;

import de.pnp.manager.component.attributes.PrimaryAttribute;
import de.pnp.manager.component.attributes.SecondaryAttribute;
import de.pnp.manager.component.character.Talent;
import de.pnp.manager.component.spell.Spell;
import de.pnp.manager.server.database.attributes.PrimaryAttributeRepository;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
        PrimaryAttribute primaryAttribute = primaryAttributeRepository.insert(getUniverseName(),
            new PrimaryAttribute(null, "Primary", "PRI"));

        Talent earthMagic = talentRepository.insert(getUniverseName(),
            new Talent(null, "Earth Magic", "Magic", primaryAttribute, primaryAttribute, primaryAttribute));
        Spell spell = createSpell().withName("Wall").withEffect("Create a Wall").withAdditionalCost("10 Mana per meter")
            .withTalent(earthMagic).build();
        Talent changedEarthMagic = new Talent(null, "Earth Magic", "Magic", primaryAttribute, primaryAttribute,
            primaryAttribute);

        testRepositoryCollectionLink(Spell::getTalents, talentRepository, spell, List.of(earthMagic),
            Map.of(earthMagic, changedEarthMagic));
    }

    @Override
    protected Spell createObject() {
        PrimaryAttribute primaryAttribute = primaryAttributeRepository.insert(getUniverseName(),
            new PrimaryAttribute(null, "Primary", "PRI"));
        SecondaryAttribute mana = createSecondaryAttribute().withName("Mana").isConsumable().persist().build();
        Talent fireMagic = talentRepository.insert(getUniverseName(),
            new Talent(null, "Fire Magic", "Magic", primaryAttribute, primaryAttribute, primaryAttribute));

        return createSpell().withName("Fireball").withEffect("Throw a fireball").withCost(10, mana)
            .withTalent(fireMagic).withTier(1).build();
    }

    @Override
    protected Spell createSlightlyChangeObject() {
        PrimaryAttribute primaryAttribute = primaryAttributeRepository.insert(getUniverseName(),
            new PrimaryAttribute(null, "Other", "OT"));
        SecondaryAttribute life = createSecondaryAttribute().withName("Life").isConsumable().persist().build();
        Talent fireMagic = talentRepository.insert(getUniverseName(),
            new Talent(null, "Fire Magic", "Magic", primaryAttribute, primaryAttribute, primaryAttribute));

        return createSpell().withName("Big Fireball").withEffect("Throw a fireball").withCost(10, life)
            .withTalent(fireMagic).withTier(3).build();
    }

    @Override
    protected List<Spell> createMultipleObjects() {
        PrimaryAttribute primaryAttribute = primaryAttributeRepository.insert(getUniverseName(),
            new PrimaryAttribute(null, "Primary", "PRI"));
        Talent earthMagic = talentRepository.insert(getUniverseName(),
            new Talent(null, "Earth Magic", "Magic", primaryAttribute, primaryAttribute, primaryAttribute));

        return List.of(
            createSpell().withName("Wall").withEffect("Creates a wall").withTalent(earthMagic).withTier(2).build(),
            createSpell().withName("Stone").withEffect("Throws a stone").withTalent(earthMagic).withTier(1).build()
        );
    }
}
