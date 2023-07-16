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
    protected float wear;

    public DamageableEquipment(float stackSize, I item, float wear) {
        super(stackSize, item);
        this.wear = wear;
    }

    @Override
    public void applyWear(float wear) {
        this.wear += wear;
    }

    @Override
    public void repair() {
        wear = 0;
    }

    @Override
    public float getRelativeDurability() {
        return Math.max(0, (getMaxDurability() - wear) / getMaxDurability());
    }

    /**
     * Returns the maximal durability.
     */
    public abstract int getMaxDurability();
}
