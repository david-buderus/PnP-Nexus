package de.pnp.manager.utils;

import de.pnp.manager.component.item.ItemType;
import de.pnp.manager.component.item.ItemType.ETypeRestriction;
import de.pnp.manager.component.upgrade.Upgrade;
import de.pnp.manager.component.upgrade.effect.UpgradeEffect;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Helper class to build {@link Upgrade}.
 */
public class TestUpgradeBuilder {

    /**
     * Returns a {@link TestUpgradeBuilder} with default values.
     */
    public static TestUpgradeBuilder createUpgrade() {
        return new TestUpgradeBuilder();
    }

    private String name;

    private ItemType type;

    private int slots;

    private int vendorPrice;

    private Collection<UpgradeEffect> effects;


    public TestUpgradeBuilder() {
        name = "Test";
        type = new ItemType(null, "Test", ETypeRestriction.ITEM);
        slots = 1;
        vendorPrice = 10;
        effects = new ArrayList<>();
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
     * Builds an {@link Upgrade} matching this builder.
     */
    public Upgrade build() {
        return new Upgrade(null, name, type, slots, vendorPrice, effects);
    }
}
