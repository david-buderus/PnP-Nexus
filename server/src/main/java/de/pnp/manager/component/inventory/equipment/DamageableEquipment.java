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
     * The current durability of this {@link Equipment}.
     */
    protected int durability;

    public DamageableEquipment(float amount, I item, int durability) {
        super(amount, item);
        this.durability = durability;
    }

    @Override
    public void applyWear(int wear) {
        durability = Math.max(0, durability - wear);
    }

    @Override
    public void repair() {
        durability = getItem().getMaxDurability();
    }

    @Override
    public float getRelativeDurability() {
        return durability / (float) getItem().getMaxDurability();
    }
}
