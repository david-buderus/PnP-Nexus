package de.pnp.manager.utils;

import de.pnp.manager.component.Universe;
import de.pnp.manager.component.item.ItemType;
import de.pnp.manager.component.upgrade.Upgrade;
import de.pnp.manager.component.upgrade.effect.SimpleUpgradeEffect;
import de.pnp.manager.component.upgrade.effect.UpgradeEffect;
import de.pnp.manager.server.database.item.ItemTypeRepository;
import de.pnp.manager.server.database.upgrade.UpgradeRepository;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Helper class to build {@link Upgrade}.
 */
public class TestUpgradeBuilder extends TestBuilderBase {

    /**
     * Component wrapper for the {@link TestUpgradeBuilder}.
     */
    @Component
    public static class TestUpgradeBuilderFactory {

        @Autowired
        private ItemTypeRepository typeRepository;

        @Autowired
        private UpgradeRepository upgradeRepository;

        /**
         * Builder with default values.
         */
        public TestUpgradeBuilder createUpgradeBuilder(String universe) {
            return new TestUpgradeBuilder(universe, typeRepository, upgradeRepository);
        }
    }

    /**
     * Returns a builder without a link to a {@link Universe} or database.
     */
    public static TestUpgradeBuilder createUpgrade() {
        return new TestUpgradeBuilder(null, null, null);
    }

    private String name;

    private ItemType type;

    private int slots;

    private int vendorPrice;

    private final Collection<UpgradeEffect> effects;

    private boolean shouldGetPersisted;

    private final UpgradeRepository upgradeRepository;

    public TestUpgradeBuilder(String universe, ItemTypeRepository typeRepository, UpgradeRepository upgradeRepository) {
        super(universe, typeRepository);
        this.upgradeRepository = upgradeRepository;
        name = "Test";
        type = getType("Test");
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
     * @see Upgrade#getTarget()
     */
    public TestUpgradeBuilder withTarget(ItemType target) {
        this.type = target;
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
            effects.add(SimpleUpgradeEffect.create("default effect"));
        }
        Upgrade upgrade = new Upgrade(null, name, type, slots, vendorPrice, effects);
        if (shouldGetPersisted) {
            return upgradeRepository.insert(universe, upgrade);
        }
        return upgrade;
    }
}
