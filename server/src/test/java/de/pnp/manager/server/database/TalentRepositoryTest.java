package de.pnp.manager.server.database;

import de.pnp.manager.component.attributes.PrimaryAttribute;
import de.pnp.manager.component.character.Talent;
import de.pnp.manager.server.database.attributes.PrimaryAttributeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static de.pnp.manager.utils.TestUtils.tagSet;

/**
 * Tests for {@link TalentRepository}.
 */
class TalentRepositoryTest extends RepositoryTestBase<Talent, TalentRepository> {

    @Autowired
    private PrimaryAttributeRepository primaryAttributeRepository;

    public TalentRepositoryTest(@Autowired TalentRepository repository) {
        super(repository);
    }

    @Test
    void testFirstAttributeLink() {
        PrimaryAttribute strength = primaryAttributeRepository.insert(getUniverseName(),
                new PrimaryAttribute(null, "Strength", "ST"));
        Talent talent = new Talent(null, "Physical Power", tagSet("Physical"), strength, strength, strength);
        PrimaryAttribute changedStrength = new PrimaryAttribute(null, "Strength", "STR");

        testRepositoryLink(Talent::getFirstAttribute, primaryAttributeRepository, talent, strength, changedStrength);
    }

    @Test
    void testSecondAttributeLink() {
        PrimaryAttribute strength = primaryAttributeRepository.insert(getUniverseName(),
                new PrimaryAttribute(null, "Strength", "ST"));
        Talent talent = new Talent(null, "Physical Power", tagSet("Physical"), strength, strength, strength);
        PrimaryAttribute changedStrength = new PrimaryAttribute(null, "Strength", "STR");

        testRepositoryLink(Talent::getSecondAttribute, primaryAttributeRepository, talent, strength, changedStrength);
    }

    @Test
    void testThirdAttributeLink() {
        PrimaryAttribute strength = primaryAttributeRepository.insert(getUniverseName(),
                new PrimaryAttribute(null, "Strength", "ST"));
        Talent talent = new Talent(null, "Physical Power", tagSet("Physical"), strength, strength, strength);
        PrimaryAttribute changedStrength = new PrimaryAttribute(null, "Strength", "STR");

        testRepositoryLink(Talent::getThirdAttribute, primaryAttributeRepository, talent, strength, changedStrength);
    }

    @Override
    protected Talent createObject() {
        PrimaryAttribute primaryAttribute = primaryAttributeRepository.insert(getUniverseName(),
                new PrimaryAttribute(null, "Primary", "PRI"));
        return new Talent(null, "Climbing", tagSet("Physical"), primaryAttribute, primaryAttribute, primaryAttribute);
    }

    @Override
    protected Talent createSlightlyChangeObject() {
        PrimaryAttribute primaryAttribute = primaryAttributeRepository.insert(getUniverseName(),
                new PrimaryAttribute(null, "Other", "OTH"));
        return new Talent(null, "Climbing", tagSet("Physical"), primaryAttribute, primaryAttribute, primaryAttribute);
    }

    @Override
    protected List<Talent> createMultipleObjects() {
        PrimaryAttribute primaryAttribute = primaryAttributeRepository.insert(getUniverseName(),
                new PrimaryAttribute(null, "Primary", "PRI"));
        return List.of(new Talent(null, "Climbing", tagSet("Physical"), primaryAttribute, primaryAttribute, primaryAttribute),
                new Talent(null, "Magic", tagSet("Magic"), primaryAttribute, primaryAttribute, primaryAttribute));
    }
}