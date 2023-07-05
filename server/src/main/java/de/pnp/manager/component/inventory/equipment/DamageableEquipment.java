package de.pnp.manager.component.inventory.equipment;

import de.pnp.manager.component.inventory.equipment.interfaces.IDamageableEquipment;
import de.pnp.manager.component.item.interfaces.IDamageableItem;
import de.pnp.manager.component.item.interfaces.IEquipableItem;

/**
 * Represents an {@link IDamageableItem} that can be held and used.
 */
public abstract class DamageableEquipment<I extends IDamageableItem & IEquipableItem> extends
    Equipment<I> implements IDamageableEquipment {

    /**
     * The current wear of this {@link Equipment}.
     */
    protected int wear;

    /**
     * The factor how much the wear damages the equipment. The higher the factor, the lower the influence.
     * <p>
     * A wear factor of {@code -1} means that wear has no influence on the equipment.
     */
    protected transient int wearFactor = -1;

    public DamageableEquipment(float stackSize, I item, int wear) {
        super(stackSize, item);
        this.wear = wear;
    }

    @Override
    public void applyWear(int wear) {
        if (wearFactor == -1) {
            return;
        }
        this.wear += wear;
    }

    @Override
    public void repair() {
        wear = 0;
    }

    @Override
    public float getRelativeDurability() {
        if (wearFactor == -1) {
            return 1;
        }
        return (getMaxDurability() - (wear / (float) wearFactor)) / getMaxDurability();
    }

    /**
     * Returns the maximal durability.
     */
    public abstract int getMaxDurability();

    public void setWearFactor(int wearFactor) {
        this.wearFactor = wearFactor;
    }
}
