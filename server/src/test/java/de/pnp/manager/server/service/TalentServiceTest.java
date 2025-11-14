package de.pnp.manager.server.service;

import de.pnp.manager.component.attributes.PrimaryAttribute;
import de.pnp.manager.component.character.Talent;
import de.pnp.manager.server.database.TalentRepository;
import de.pnp.manager.server.database.attributes.PrimaryAttributeRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static de.pnp.manager.utils.TestUtils.tagSet;

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
        PrimaryAttribute primaryAttribute = primaryAttributeRepository.insert(getUniverseId(),
                new PrimaryAttribute(null, "Primary", "PRI"));
        return List.of(
                new Talent(null, "Alchemy", tagSet("Knowledge"), primaryAttribute, primaryAttribute, primaryAttribute),
                new Talent(null, "Swimming", tagSet("Physical"), primaryAttribute, primaryAttribute, primaryAttribute),
                new Talent(null, "Magical Knowledge", tagSet("Magic"), primaryAttribute, primaryAttribute, primaryAttribute)
        );
    }
}
