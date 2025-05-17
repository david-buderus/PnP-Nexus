package de.pnp.manager.server.service;

import de.pnp.manager.component.attributes.PrimaryAttribute;
import de.pnp.manager.component.attributes.SecondaryAttribute;
import de.pnp.manager.component.character.Talent;
import de.pnp.manager.component.spell.ECastingType;
import de.pnp.manager.component.spell.Spell;
import de.pnp.manager.server.database.SpellRepository;
import de.pnp.manager.server.database.TalentRepository;
import de.pnp.manager.server.database.attributes.PrimaryAttributeRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests for {@link SpellService}.
 */
public class SpellServiceTest extends RepositoryServiceBaseTest<Spell, SpellRepository, SpellService> {

    @Autowired
    private PrimaryAttributeRepository primaryAttributeRepository;

    @Autowired
    private TalentRepository talentRepository;

    public SpellServiceTest(@Autowired SpellService spellService, @Autowired SpellRepository repository) {
        super(spellService, repository, Spell.class);
    }

    @Override
    protected List<Spell> createObjects() {
        PrimaryAttribute primaryAttribute = primaryAttributeRepository.insert(getUniverseName(),
            new PrimaryAttribute(null, "Primary", "PRI"));
        SecondaryAttribute secondaryAttribute = createSecondaryAttribute().withName("Secondary").isConsumable()
            .withFormula("1 * PRI").addDependency(primaryAttribute).persist().build();

        Talent fireTalent = talentRepository.insert(getUniverseName(),
            new Talent(null, "Fire", "Magic", primaryAttribute, primaryAttribute, primaryAttribute));
        Talent lightningTalent = talentRepository.insert(getUniverseName(),
            new Talent(null, "Lightning", "Magic", primaryAttribute, primaryAttribute, primaryAttribute));

        return List.of(
            createSpell().withName("Fireball").withEffect("Throws a fireball").withCost(10, secondaryAttribute)
                .withCastTime(2).withTalent(fireTalent).withTier(3).withCastingType(
                    ECastingType.SOMATIC).build(),
            createSpell().withName("Spark").withEffect("Light a fire").withCost(2, secondaryAttribute)
                .withTalent(fireTalent).withTier(1).build(),
            createSpell().withName("Lightning Fire").withEffect("Fire with lightning").withCost(100, secondaryAttribute)
                .withAdditionalCost("90 Mana per Round").withCastTime(10).withCastingType(ECastingType.VERBAL)
                .withTalent(fireTalent).withTalent(lightningTalent).withTier(3).build()
        );
    }
}
