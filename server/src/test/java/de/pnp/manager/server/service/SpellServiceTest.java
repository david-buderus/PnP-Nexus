package de.pnp.manager.server.service;

import de.pnp.manager.component.Spell;
import de.pnp.manager.component.attributes.EPrimaryAttribute;
import de.pnp.manager.component.character.Talent;
import de.pnp.manager.server.database.SpellRepository;
import de.pnp.manager.server.database.TalentRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests for {@link SpellService}.
 */
public class SpellServiceTest extends RepositoryServiceBaseTest<Spell, SpellRepository, SpellService> {

    @Autowired
    private TalentRepository talentRepository;

    public SpellServiceTest(@Autowired SpellService spellService) {
        super(spellService, Spell.class);
    }

    @Override
    protected List<Spell> createObjects() {
        Talent fireTalent = talentRepository.insert(getUniverseName(),
            new Talent(null, "Fire", "Magic", EPrimaryAttribute.INTELLIGENCE, EPrimaryAttribute.INTELLIGENCE,
                EPrimaryAttribute.CHARISMA));
        Talent lightningTalent = talentRepository.insert(getUniverseName(),
            new Talent(null, "Lightning", "Magic", EPrimaryAttribute.INTELLIGENCE, EPrimaryAttribute.PRECISION,
                EPrimaryAttribute.CHARISMA));
        return List.of(
            new Spell(null, "Fireball", "Throws a fireball", "10 Mana", "2 Rounds", List.of(fireTalent), 3),
            new Spell(null, "Spark", "Light a fire", "2 Mana", "", List.of(fireTalent), 1),
            new Spell(null, "Lightning Fire", "Fire with lightning", "1000 Mana", "10 Rounds",
                List.of(fireTalent, lightningTalent), 3)
        );
    }
}
