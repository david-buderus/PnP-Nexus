package de.pnp.manager.server.database;

import static org.assertj.core.api.Assertions.assertThat;

import de.pnp.manager.component.Spell;
import de.pnp.manager.component.attributes.EPrimaryAttribute;
import de.pnp.manager.component.character.Talent;
import java.util.List;
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
    Talent earthMagic = talentRepository.insert(universe,
        new Talent(null, "Earth Magic", "Magic", EPrimaryAttribute.INTELLIGENCE,
            EPrimaryAttribute.STRENGTH, EPrimaryAttribute.CHARISMA));

    Spell spell = repository.insert(universe,
        new Spell(null, "Wall", "Create a wall", "10 Mana per meter", "1 per meter",
            List.of(earthMagic), 2));
    assertThat(spell.getTalents()).containsExactly(earthMagic);

    Talent changedEarthMagic = talentRepository.update(universe, earthMagic.getId(),
        new Talent(null, "Earth Magic", "Magic", EPrimaryAttribute.INTELLIGENCE,
            EPrimaryAttribute.STRENGTH, EPrimaryAttribute.RESILIENCE));

    spell = repository.get(universe, spell.getId()).orElseThrow();
    assertThat(spell.getTalents()).containsExactly(changedEarthMagic);
  }

  @Override
  protected Spell createObject() {
    Talent fireMagic = talentRepository.insert(universe,
        new Talent(null, "Fire Magic", "Magic", EPrimaryAttribute.INTELLIGENCE,
            EPrimaryAttribute.INTELLIGENCE, EPrimaryAttribute.CHARISMA));

    return new Spell(null, "Fireball", "Throw a fireball", "10 Mana", "0",
        List.of(fireMagic), 2);
  }

  @Override
  protected Spell createSlightlyChangeObject() {
    Talent fireMagic = talentRepository.getByName(universe, "Fire Magic").stream().findFirst()
        .orElseThrow();

    return new Spell(null, "Big Fireball", "Throw a fireball", "30 Mana", "1",
        List.of(fireMagic), 3);
  }
}
