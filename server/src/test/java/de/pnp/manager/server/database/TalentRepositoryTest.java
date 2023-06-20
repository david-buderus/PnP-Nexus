package de.pnp.manager.server.database;

import de.pnp.manager.component.attributes.EPrimaryAttribute;
import de.pnp.manager.component.character.Talent;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests for {@link TalentRepository}.
 */
class TalentRepositoryTest extends RepositoryTestBase<Talent, TalentRepository> {

  public TalentRepositoryTest(@Autowired TalentRepository repository) {
    super(repository);
  }

  @Override
  protected Talent createObject() {
    return new Talent(null, "Climbing", "Physical", EPrimaryAttribute.STRENGTH,
        EPrimaryAttribute.ENDURANCE, EPrimaryAttribute.ENDURANCE);
  }

  @Override
  protected Talent createSlightlyChangeObject() {
    return new Talent(null, "Climbing", "Physical", EPrimaryAttribute.STRENGTH,
        EPrimaryAttribute.DEXTERITY, EPrimaryAttribute.ENDURANCE);
  }
}