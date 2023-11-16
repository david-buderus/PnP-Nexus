package de.pnp.manager.server.service;

import de.pnp.manager.component.attributes.EPrimaryAttribute;
import de.pnp.manager.component.character.Talent;
import de.pnp.manager.server.database.TalentRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests for {@link TalentService}.
 */
public class TalentServiceTest extends RepositoryServiceBaseTest<Talent, TalentRepository, TalentService> {

    public TalentServiceTest(@Autowired TalentService talentService, @Autowired TalentRepository repository) {
        super(talentService, repository, Talent.class);
    }

    @Override
    protected List<Talent> createObjects() {
        return List.of(
            new Talent(null, "Alchemy", "Knowledge", EPrimaryAttribute.INTELLIGENCE, EPrimaryAttribute.DEXTERITY,
                EPrimaryAttribute.ENDURANCE),
            new Talent(null, "Swimming", "Physical", EPrimaryAttribute.STRENGTH, EPrimaryAttribute.DEXTERITY,
                EPrimaryAttribute.ENDURANCE),
            new Talent(null, "Magical Knowledge", "Magic", EPrimaryAttribute.INTELLIGENCE,
                EPrimaryAttribute.INTELLIGENCE, EPrimaryAttribute.CHARISMA)
        );
    }
}
