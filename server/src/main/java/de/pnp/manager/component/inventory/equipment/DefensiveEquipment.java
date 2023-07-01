package de.pnp.manager.component.inventory.equipment;

import de.pnp.manager.component.inventory.equipment.interfaces.IHandheldEquipment;
import de.pnp.manager.component.item.interfaces.IDefensiveItem;
import de.pnp.manager.component.item.interfaces.IHandheldItem;

public class DefensiveEquipment extends DamageableEquipment<IDefensiveItem> implements
    IHandheldEquipment {

    public DefensiveEquipment(IDefensiveItem item, int durability) {
        super(item, durability);
    }

    public int getDefense() {
        return Math.round(getItem().getArmor() * getRelativeDurability());
    }

    @Override
    public int getHit() {
        throw new AssertionError(
            "A IHandholdItem which is an DefensiveEquipment is always a shield and thus does not have hit.");
    }

    @Override
    public float getInitiative() {
        if (getItem() instanceof IHandheldItem handHoldItem) {
            return handHoldItem.getInitiative();
        }
        throw new AssertionError(
            "Only HandholdItems have initiative.");
    }
}
