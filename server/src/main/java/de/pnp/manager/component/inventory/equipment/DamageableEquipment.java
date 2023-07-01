package de.pnp.manager.component.inventory.equipment;

import de.pnp.manager.component.inventory.equipment.interfaces.IDamageableEquipment;
import de.pnp.manager.component.item.interfaces.IDamageableItem;
import de.pnp.manager.component.item.interfaces.IEquipableItem;

public class DamageableEquipment<I extends IDamageableItem & IEquipableItem> extends
    Equipment<I> implements
    IDamageableEquipment {

    protected int durability;

    public DamageableEquipment(I item, int durability) {
        super(item);
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
