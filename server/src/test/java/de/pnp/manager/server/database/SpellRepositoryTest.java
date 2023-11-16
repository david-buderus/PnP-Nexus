package de.pnp.manager.server.database;

import de.pnp.manager.component.Spell;
import de.pnp.manager.component.attributes.PrimaryAttribute;
import de.pnp.manager.component.character.Talent;
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
        Spell spell = new Spell(null, "Wall", "Create a wall", "10 Mana per meter", "1 per meter",
            List.of(earthMagic), 2);
        Talent changedEarthMagic = new Talent(null, "Earth Magic", "Magic", primaryAttribute, primaryAttribute,
            primaryAttribute);

        testRepositoryCollectionLink(Spell::getTalents, talentRepository, spell, List.of(earthMagic),
            Map.of(earthMagic, changedEarthMagic));
    }

    @Override
    protected Spell createObject() {
        PrimaryAttribute primaryAttribute = primaryAttributeRepository.insert(getUniverseName(),
            new PrimaryAttribute(null, "Primary", "PRI"));
        Talent fireMagic = talentRepository.insert(getUniverseName(),
            new Talent(null, "Fire Magic", "Magic", primaryAttribute, primaryAttribute, primaryAttribute));

        return new Spell(null, "Fireball", "Throw a fireball", "10 Mana", "0",
            List.of(fireMagic), 2);
    }

    @Override
    protected Spell createSlightlyChangeObject() {
        PrimaryAttribute primaryAttribute = primaryAttributeRepository.insert(getUniverseName(),
            new PrimaryAttribute(null, "Other", "OT"));
        Talent fireMagic = talentRepository.insert(getUniverseName(),
            new Talent(null, "Fire Magic", "Magic", primaryAttribute, primaryAttribute, primaryAttribute));

        return new Spell(null, "Big Fireball", "Throw a fireball", "30 Mana", "1",
            List.of(fireMagic), 3);
    }

    @Override
    protected List<Spell> createMultipleObjects() {
        PrimaryAttribute primaryAttribute = primaryAttributeRepository.insert(getUniverseName(),
            new PrimaryAttribute(null, "Primary", "PRI"));
        Talent earthMagic = talentRepository.insert(getUniverseName(),
            new Talent(null, "Earth Magic", "Magic", primaryAttribute, primaryAttribute, primaryAttribute));

        return List.of(new Spell(null, "Wall", "Creates a wall", "", "", List.of(earthMagic), 2),
            new Spell(null, "Stone", "Throws a stone", "", "", List.of(earthMagic), 1));
    }
}
