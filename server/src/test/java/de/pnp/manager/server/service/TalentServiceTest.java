package de.pnp.manager.server.service;

import de.pnp.manager.component.attributes.PrimaryAttribute;
import de.pnp.manager.component.character.Talent;
import de.pnp.manager.server.database.TalentRepository;
import de.pnp.manager.server.database.attributes.PrimaryAttributeRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests for {@link TalentService}.
 */
public class TalentServiceTest extends RepositoryServiceBaseTest<Talent, TalentRepository, TalentService> {

    @Autowired
    private PrimaryAttributeRepository primaryAttributeRepository;

    public TalentServiceTest(@Autowired TalentService talentService, @Autowired TalentRepository repository) {
        super(talentService, repository, Talent.class);
    }

    @Override
    protected List<Talent> createObjects() {
        PrimaryAttribute primaryAttribute = primaryAttributeRepository.insert(getUniverseName(),
            new PrimaryAttribute(null, "Primary", "PRI"));
        return List.of(
            new Talent(null, "Alchemy", "Knowledge", primaryAttribute, primaryAttribute, primaryAttribute),
            new Talent(null, "Swimming", "Physical", primaryAttribute, primaryAttribute, primaryAttribute),
            new Talent(null, "Magical Knowledge", "Magic", primaryAttribute, primaryAttribute, primaryAttribute)
        );
    }
}
