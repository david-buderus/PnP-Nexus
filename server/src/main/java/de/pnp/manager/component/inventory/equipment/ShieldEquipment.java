package de.pnp.manager.component.inventory.equipment;

import de.pnp.manager.component.inventory.equipment.interfaces.IHandheldEquipment;
import de.pnp.manager.component.item.equipable.Shield;
import de.pnp.manager.component.upgrade.effect.EItemEquipmentManipulator;

/**
 * Represents an {@link Shield} that can be held and used.
 */
public class ShieldEquipment extends DefensiveEquipment<Shield> implements IHandheldEquipment {

    public ShieldEquipment(float stackSize, Shield item, float wear) {
        super(stackSize, item, wear);
    }

    @Override
    public int getHit() {
        return applyItemEffects(EItemEquipmentManipulator.HIT, getItem().getHit());
    }

    @Override
    public float getInitiative() {
        return applyItemEffects(EItemEquipmentManipulator.INITIATIVE, getItem().getInitiative());
    }
}
