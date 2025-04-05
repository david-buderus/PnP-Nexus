package de.pnp.manager.utils;

import de.pnp.manager.component.TagRequirement;
import de.pnp.manager.component.universe.Universe;
import de.pnp.manager.component.upgrade.EUpgradeRestriction;
import de.pnp.manager.component.upgrade.Upgrade;
import de.pnp.manager.component.upgrade.effect.SimpleUpgradeEffect;
import de.pnp.manager.component.upgrade.effect.UpgradeEffect;
import de.pnp.manager.server.database.upgrade.UpgradeRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Helper class to build {@link Upgrade}.
 */
public class TestUpgradeBuilder {

    /**
     * Component wrapper for the {@link TestUpgradeBuilder}.
     */
    @Component
    public static class TestUpgradeBuilderFactory {

        @Autowired
        private UpgradeRepository upgradeRepository;

        /**
         * Builder with default values.
         */
        public TestUpgradeBuilder createUpgradeBuilder(String universe) {
            return new TestUpgradeBuilder(universe, upgradeRepository);
        }
    }

    /**
     * Returns a builder without a link to a {@link Universe} or database.
     */
    public static TestUpgradeBuilder createUpgrade() {
        return new TestUpgradeBuilder(null, null);
    }

    private final String universe;

    private String name;

    private EUpgradeRestriction restriction;

    private TagRequirement tagRequirement;

    private int slots;

    private int vendorPrice;

    private final Collection<UpgradeEffect> effects;

    private boolean shouldGetPersisted;

    private final UpgradeRepository upgradeRepository;

    public TestUpgradeBuilder(String universe, UpgradeRepository upgradeRepository) {
        this.universe = universe;
        this.upgradeRepository = upgradeRepository;
        name = "Test";
        restriction = EUpgradeRestriction.ITEM;
        tagRequirement = TagRequirement.NO_REQUIREMENT;
        slots = 1;
        vendorPrice = 10;
        effects = new ArrayList<>();
        shouldGetPersisted = false;
    }

    /**
     * @see Upgrade#getName()
     */
    public TestUpgradeBuilder withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * @see Upgrade#getRestriction()
     */
    public TestUpgradeBuilder withRestriction(EUpgradeRestriction restriction) {
        this.restriction = restriction;
        return this;
    }

    /**
     * @see Upgrade#getTagRequirement()
     */
    public TestUpgradeBuilder withNecessaryTags(String... tags) {
        this.tagRequirement = TagRequirement.from(List.of(Set.of(tags)));
        return this;
    }

    /**
     * @see Upgrade#getSlots()
     */
    public TestUpgradeBuilder withSlots(int slots) {
        this.slots = slots;
        return this;
    }

    /**
     * @see Upgrade#getEffects()
     */
    public TestUpgradeBuilder addEffect(UpgradeEffect effect) {
        this.effects.add(effect);
        return this;
    }

    /**
     * Sets that the resulting {@link Upgrade} will be persisted.
     */
    public TestUpgradeBuilder persist() {
        this.shouldGetPersisted = true;
        return this;
    }

    /**
     * Builds an {@link Upgrade} matching this builder.
     */
    public Upgrade build() {
        if (effects.isEmpty()) {
            effects.add(new SimpleUpgradeEffect("default effect"));
        }
        Upgrade upgrade = new Upgrade(null, name, restriction, tagRequirement, slots, vendorPrice, effects);
        if (shouldGetPersisted) {
            return upgradeRepository.insert(universe, upgrade);
        }
        return upgrade;
    }
}
