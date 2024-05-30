package de.pnp.manager.utils;

import de.pnp.manager.component.attributes.PrimaryAttribute;
import de.pnp.manager.component.attributes.SecondaryAttribute;
import de.pnp.manager.component.attributes.SecondaryAttribute.PrimaryAttributeDependency;
import de.pnp.manager.server.database.attributes.PrimaryAttributeRepository;
import de.pnp.manager.server.database.attributes.SecondaryAttributeRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        public TestSecondaryAttributeBuilder createAttributeBuilder(String universe) {
            return new TestSecondaryAttributeBuilder(universe, primaryAttributeRepository,
                secondaryAttributeRepository);
        }
    }

    /**
     * Name of the connected universe.
     */
    private final String universe;

    private final PrimaryAttributeRepository primaryAttributeRepository;

    private final SecondaryAttributeRepository secondaryAttributeRepository;

    private String name;

    private boolean consumable;

    private final Collection<PrimaryAttributeDependency> dependencies;

    private boolean shouldGetPersisted;

    private TestSecondaryAttributeBuilder(String universe, PrimaryAttributeRepository primaryAttributeRepository,
        SecondaryAttributeRepository secondaryAttributeRepository) {
        this.universe = universe;
        this.primaryAttributeRepository = primaryAttributeRepository;
        this.secondaryAttributeRepository = secondaryAttributeRepository;

        name = "Sec Attribute";
        consumable = false;
        dependencies = new ArrayList<>();
    }

    /**
     * @see SecondaryAttribute#getName()
     */
    public TestSecondaryAttributeBuilder withName(String name) {
        this.name = name;
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
     * @see SecondaryAttribute#getPrimaryAttributeDependencies()
     */
    public TestSecondaryAttributeBuilder addDependency(float factor, PrimaryAttribute primaryAttribute) {
        this.dependencies.add(new PrimaryAttributeDependency(factor, primaryAttribute));
        return this;
    }

    /**
     * @see SecondaryAttribute#getPrimaryAttributeDependencies()
     */
    public TestSecondaryAttributeBuilder addDependency(float factor, String primaryAttribute) {
        this.dependencies.add(new PrimaryAttributeDependency(factor, getPrimaryAttribute(primaryAttribute)));
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
        Collection<PrimaryAttributeDependency> actualDependencies = dependencies;
        if (dependencies.isEmpty()) {
            actualDependencies = List.of(new PrimaryAttributeDependency(1, getPrimaryAttribute("Example Attribute")));
        }
        SecondaryAttribute attribute = new SecondaryAttribute(null, name, consumable, actualDependencies);
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
