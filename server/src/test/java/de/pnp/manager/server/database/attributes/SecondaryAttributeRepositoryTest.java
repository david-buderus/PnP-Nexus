package de.pnp.manager.server.database.attributes;

import de.pnp.manager.component.attributes.PrimaryAttribute;
import de.pnp.manager.component.attributes.SecondaryAttribute;
import de.pnp.manager.component.math.IExpressionVariable.PrimaryAttributeVariable;
import de.pnp.manager.server.database.RepositoryTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

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
        PrimaryAttribute strength = primaryAttributeRepository.insert(getUniverseId(),
                new PrimaryAttribute(null, "Strength", "ST"));
        SecondaryAttribute power = createSecondaryAttribute().withName("Power").addDependency(strength)
                .withFormula("10 * ST").build();

        PrimaryAttribute changedStrength = new PrimaryAttribute(null, "Strength", "STR");

        testRepositoryCollectionLink(
                secondaryAttribute -> secondaryAttribute.getCalculationFormula().getVariables().stream()
                        .filter(PrimaryAttributeVariable.class::isInstance)
                        .map(variable -> ((PrimaryAttributeVariable) variable).attribute()).toList(),
                primaryAttributeRepository, power, List.of(strength), Map.of(strength, changedStrength));
    }

    @Override
    protected SecondaryAttribute createObject() {
        PrimaryAttribute strength = primaryAttributeRepository.insert(getUniverseId(),
                new PrimaryAttribute(null, "Strength", "STR"));
        return createSecondaryAttribute().withName("Power").addDependency(strength).withFormula("10 * ST").build();
    }

    @Override
    protected SecondaryAttribute createSlightlyChangeObject() {
        PrimaryAttribute endurance = primaryAttributeRepository.insert(getUniverseId(),
                new PrimaryAttribute(null, "Endurance", "END"));
        return createSecondaryAttribute().withName("Power").isConsumable().addDependency(endurance)
                .withFormula("3 * END").build();
    }

    @Override
    protected List<SecondaryAttribute> createMultipleObjects() {
        PrimaryAttribute strength = primaryAttributeRepository.insert(getUniverseId(),
                new PrimaryAttribute(null, "Strength", "STR"));
        return List.of(
                createSecondaryAttribute().withName("Power").addDependency(strength).withFormula("2 * STR").build(),
                createSecondaryAttribute().withName("Health").isConsumable().addDependency(strength).withFormula("10 * STR")
                        .build());
    }
}
