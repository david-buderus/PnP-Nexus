package de.pnp.manager.server.database;

import de.pnp.manager.component.Spell;
import de.pnp.manager.component.attributes.EPrimaryAttribute;
import de.pnp.manager.component.character.Talent;
import java.util.Collection;
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

    public SpellRepositoryTest(@Autowired SpellRepository repository) {
        super(repository);
    }

    @Test
    void testTalentLink() {
        Talent earthMagic = talentRepository.insert(universeName,
            new Talent(null, "Earth Magic", "Magic", EPrimaryAttribute.INTELLIGENCE,
                EPrimaryAttribute.STRENGTH, EPrimaryAttribute.CHARISMA));
        Spell spell = new Spell(null, "Wall", "Create a wall", "10 Mana per meter", "1 per meter",
            List.of(earthMagic), 2);
        Talent changedEarthMagic = new Talent(null, "Earth Magic", "Magic",
            EPrimaryAttribute.INTELLIGENCE,
            EPrimaryAttribute.STRENGTH, EPrimaryAttribute.RESILIENCE);

        testRepositoryCollectionLink(Spell::getTalents, talentRepository, spell, List.of(earthMagic),
            Map.of(earthMagic, changedEarthMagic));
    }

    @Override
    protected Spell createObject() {
        Talent fireMagic = talentRepository.insert(universeName,
            new Talent(null, "Fire Magic", "Magic", EPrimaryAttribute.INTELLIGENCE,
                EPrimaryAttribute.INTELLIGENCE, EPrimaryAttribute.CHARISMA));

        return new Spell(null, "Fireball", "Throw a fireball", "10 Mana", "0",
            List.of(fireMagic), 2);
    }

    @Override
    protected Spell createSlightlyChangeObject() {
        Talent fireMagic = talentRepository.insert(universeName,
            new Talent(null, "Fire Magic", "Magic", EPrimaryAttribute.INTELLIGENCE,
                EPrimaryAttribute.INTELLIGENCE, EPrimaryAttribute.CHARISMA));

        return new Spell(null, "Big Fireball", "Throw a fireball", "30 Mana", "1",
            List.of(fireMagic), 3);
    }

    @Override
    protected Collection<Spell> createMultipleObjects() {
        return List.of(new Spell(null, "Wall", "", "", "", List.of(), 2),
            new Spell(null, "Shock", "", "", "", List.of(), 1));
    }
}
