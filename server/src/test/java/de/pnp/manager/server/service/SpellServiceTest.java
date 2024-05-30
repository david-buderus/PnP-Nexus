package de.pnp.manager.server.service;

import de.pnp.manager.component.IResourceUsage.CharacterResourceUsage;
import de.pnp.manager.component.Spell;
import de.pnp.manager.component.attributes.PrimaryAttribute;
import de.pnp.manager.component.attributes.SecondaryAttribute;
import de.pnp.manager.component.attributes.SecondaryAttribute.PrimaryAttributeDependency;
import de.pnp.manager.component.character.Talent;
import de.pnp.manager.server.database.SpellRepository;
import de.pnp.manager.server.database.TalentRepository;
import de.pnp.manager.server.database.attributes.PrimaryAttributeRepository;
import de.pnp.manager.server.database.attributes.SecondaryAttributeRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests for {@link SpellService}.
 */
public class SpellServiceTest extends RepositoryServiceBaseTest<Spell, SpellRepository, SpellService> {

    @Autowired
    private PrimaryAttributeRepository primaryAttributeRepository;

    @Autowired
    private SecondaryAttributeRepository secondaryAttributeRepository;

    @Autowired
    private TalentRepository talentRepository;

    public SpellServiceTest(@Autowired SpellService spellService, @Autowired SpellRepository repository) {
        super(spellService, repository, Spell.class);
    }

    @Override
    protected List<Spell> createObjects() {
        PrimaryAttribute primaryAttribute = primaryAttributeRepository.insert(getUniverseName(),
            new PrimaryAttribute(null, "Primary", "PRI"));
        SecondaryAttribute secondaryAttribute = secondaryAttributeRepository.insert(getUniverseName(),
            new SecondaryAttribute(null, "Secondary", true,
                List.of(new PrimaryAttributeDependency(1, primaryAttribute))));
        Talent fireTalent = talentRepository.insert(getUniverseName(),
            new Talent(null, "Fire", "Magic", primaryAttribute, primaryAttribute, primaryAttribute));
        Talent lightningTalent = talentRepository.insert(getUniverseName(),
            new Talent(null, "Lightning", "Magic", primaryAttribute, primaryAttribute, primaryAttribute));
        return List.of(
            new Spell(null, "Fireball", "Throws a fireball",
                List.of(new CharacterResourceUsage(10, secondaryAttribute)),
                "", "2 Rounds", List.of(fireTalent), 3),
            new Spell(null, "Spark", "Light a fire", List.of(new CharacterResourceUsage(2, secondaryAttribute)), "", "",
                List.of(fireTalent), 1),
            new Spell(null, "Lightning Fire", "Fire with lightning",
                List.of(new CharacterResourceUsage(100, secondaryAttribute)), "90 Mana per Round", "10 Rounds",
                List.of(fireTalent, lightningTalent), 3)
        );
    }
}
