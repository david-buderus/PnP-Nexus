package de.pnp.manager.component.inventory.equipment;

import de.pnp.manager.component.inventory.equipment.interfaces.IHandheldEquipment;
import de.pnp.manager.component.item.equipable.Shield;
import de.pnp.manager.component.upgrade.effect.EUpgradeEquipmentManipulator;

/**
 * Represents an {@link Shield} that can be held and used.
 */
public class ShieldEquipment extends DefensiveEquipment<Shield> implements IHandheldEquipment {

    public ShieldEquipment(float stackSize, Shield item, int wear) {
        super(stackSize, item, wear);
    }

    @Override
    public int getHit() {
        return applyUpgradeEffects(EUpgradeEquipmentManipulator.HIT, getItem().getHit());
    }

    @Override
    public float getInitiative() {
        return applyUpgradeEffects(EUpgradeEquipmentManipulator.INITIATIVE, getItem().getInitiative());
    }
}
