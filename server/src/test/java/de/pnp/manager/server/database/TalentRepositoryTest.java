package de.pnp.manager.server.database;

import de.pnp.manager.component.attributes.EPrimaryAttribute;
import de.pnp.manager.component.character.Talent;
import java.util.List;
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

    @Override
    protected List<Talent> createMultipleObjects() {
        return List.of(new Talent(null, "Climbing", "Physical", EPrimaryAttribute.STRENGTH,
                EPrimaryAttribute.DEXTERITY, EPrimaryAttribute.ENDURANCE),
            new Talent(null, "Magic", "Magic", EPrimaryAttribute.INTELLIGENCE,
                EPrimaryAttribute.INTELLIGENCE, EPrimaryAttribute.CHARISMA));
    }
}