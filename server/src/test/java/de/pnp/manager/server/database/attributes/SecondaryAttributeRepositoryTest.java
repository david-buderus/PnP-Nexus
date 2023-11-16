package de.pnp.manager.server.database.attributes;

import de.pnp.manager.component.attributes.PrimaryAttribute;
import de.pnp.manager.component.attributes.SecondaryAttribute;
import de.pnp.manager.component.attributes.SecondaryAttribute.PrimaryAttributeDependency;
import de.pnp.manager.server.database.RepositoryTestBase;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests for {@link SecondaryAttributeRepository}
 */
class SecondaryAttributeRepositoryTest extends RepositoryTestBase<SecondaryAttribute, SecondaryAttributeRepository> {

    @Autowired
    private PrimaryAttributeRepository primaryAttributeRepository;

    public SecondaryAttributeRepositoryTest(@Autowired SecondaryAttributeRepository repository) {
        super(repository);
    }

    @Test
    void testPrimaryAttributeLink() {
        PrimaryAttribute strength = primaryAttributeRepository.insert(getUniverseName(),
            new PrimaryAttribute(null, "Strength", "ST"));
        SecondaryAttribute power = new SecondaryAttribute(null, "Power", false,
            List.of(new PrimaryAttributeDependency(10, strength)));
        PrimaryAttribute changedStrength = new PrimaryAttribute(null, "Strength", "STR");

        testRepositoryCollectionLink(
            secondaryAttribute -> secondaryAttribute.getPrimaryAttributeDependencies().stream()
                .map(PrimaryAttributeDependency::primaryAttribute).toList(),
            primaryAttributeRepository, power, List.of(strength), Map.of(strength, changedStrength));
    }

    @Override
    protected SecondaryAttribute createObject() {
        PrimaryAttribute strength = primaryAttributeRepository.insert(getUniverseName(),
            new PrimaryAttribute(null, "Strength", "STR"));
        return new SecondaryAttribute(null, "Power", false, List.of(new PrimaryAttributeDependency(10, strength)));
    }

    @Override
    protected SecondaryAttribute createSlightlyChangeObject() {
        PrimaryAttribute endurance = primaryAttributeRepository.insert(getUniverseName(),
            new PrimaryAttribute(null, "Endurance", "END"));
        return new SecondaryAttribute(null, "Power", true, List.of(new PrimaryAttributeDependency(3, endurance)));
    }

    @Override
    protected List<SecondaryAttribute> createMultipleObjects() {
        PrimaryAttribute strength = primaryAttributeRepository.insert(getUniverseName(),
            new PrimaryAttribute(null, "Strength", "STR"));
        return List.of(
            new SecondaryAttribute(null, "Power", false, List.of(new PrimaryAttributeDependency(2, strength))),
            new SecondaryAttribute(null, "Health", true, List.of(new PrimaryAttributeDependency(10, strength))));
    }
}
