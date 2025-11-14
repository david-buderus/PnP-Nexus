package de.pnp.manager.utils;

import de.pnp.manager.component.attributes.PrimaryAttribute;
import de.pnp.manager.component.attributes.SecondaryAttribute;
import de.pnp.manager.component.math.BinaryExpressionTree;
import de.pnp.manager.component.math.IExpressionVariable.PrimaryAttributeVariable;
import de.pnp.manager.component.math.IllegalFormulaException;
import de.pnp.manager.server.database.attributes.PrimaryAttributeRepository;
import de.pnp.manager.server.database.attributes.SecondaryAttributeRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Builder for {@link SecondaryAttribute}.
 */
public class TestSecondaryAttributeBuilder {

    /**
     * Component wrapper for the {@link TestSecondaryAttributeBuilder}.
     */
    @Component
    public static class TestSecondaryAttributeBuilderFactory {

        @Autowired
        private PrimaryAttributeRepository primaryAttributeRepository;

        @Autowired
        private SecondaryAttributeRepository secondaryAttributeRepository;

        /**
         * Builder with default values.
         */
        public TestSecondaryAttributeBuilder createAttributeBuilder(ObjectId universe) {
            return new TestSecondaryAttributeBuilder(universe, primaryAttributeRepository,
                    secondaryAttributeRepository);
        }
    }

    /**
     * Name of the connected universe.
     */
    private final ObjectId universe;

    private final PrimaryAttributeRepository primaryAttributeRepository;

    private final SecondaryAttributeRepository secondaryAttributeRepository;

    private String name;

    private String shortName;

    private boolean consumable;

    private String formula;

    private final Set<PrimaryAttribute> dependencies;

    private boolean shouldGetPersisted;

    private TestSecondaryAttributeBuilder(ObjectId universe, PrimaryAttributeRepository primaryAttributeRepository,
                                          SecondaryAttributeRepository secondaryAttributeRepository) {
        this.universe = universe;
        this.primaryAttributeRepository = primaryAttributeRepository;
        this.secondaryAttributeRepository = secondaryAttributeRepository;

        name = "Sec Attribute";
        shortName = "SEA";
        consumable = false;
        formula = "10";
        dependencies = new HashSet<>();
    }

    /**
     * @see SecondaryAttribute#getName()
     */
    public TestSecondaryAttributeBuilder withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * @see SecondaryAttribute#getShortName()
     */
    public TestSecondaryAttributeBuilder withShortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    /**
     * @see SecondaryAttribute#isConsumable()
     */
    public TestSecondaryAttributeBuilder isConsumable() {
        this.consumable = true;
        return this;
    }

    /**
     * @see SecondaryAttribute#getCalculationFormula()
     */
    public TestSecondaryAttributeBuilder addDependency(PrimaryAttribute primaryAttribute) {
        this.dependencies.add(primaryAttribute);
        return this;
    }

    /**
     * @see SecondaryAttribute#getCalculationFormula()
     */
    public TestSecondaryAttributeBuilder addDependency(String primaryAttribute) {
        this.dependencies.add(getPrimaryAttribute(primaryAttribute));
        return this;
    }

    /**
     * @see SecondaryAttribute#getCalculationFormula()
     */
    public TestSecondaryAttributeBuilder withFormula(String formula) {
        this.formula = formula;
        return this;
    }

    /**
     * Sets that the resulting {@link SecondaryAttribute} will be persisted.
     */
    public TestSecondaryAttributeBuilder persist() {
        this.shouldGetPersisted = true;
        return this;
    }

    /**
     * Builds the {@link SecondaryAttribute}.
     */
    public SecondaryAttribute build() {
        BinaryExpressionTree tree;
        try {
            tree = BinaryExpressionTree.from(formula,
                    dependencies.stream().map(PrimaryAttributeVariable::new).collect(Collectors.toSet()));
        } catch (IllegalFormulaException e) {
            return fail(e);
        }

        SecondaryAttribute attribute = new SecondaryAttribute(null, name, shortName, consumable, tree);
        if (shouldGetPersisted) {
            return secondaryAttributeRepository.insert(universe, attribute);
        }
        return attribute;
    }

    private PrimaryAttribute getPrimaryAttribute(String attributeName) {
        if (primaryAttributeRepository == null) {
            return new PrimaryAttribute(null, attributeName,
                    attributeName.chars().filter(Character::isUpperCase).mapToObj(i -> String.valueOf((char) i))
                            .collect(Collectors.joining()));
        }
        return primaryAttributeRepository.get(universe, attributeName).orElseGet(() ->
                primaryAttributeRepository.insert(universe,
                        new PrimaryAttribute(null, attributeName,
                                attributeName.chars().filter(Character::isUpperCase).mapToObj(i -> String.valueOf((char) i))
                                        .collect(Collectors.joining()))));
    }
}
