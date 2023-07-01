package de.pnp.manager.component.inventory.equipment.interfaces;

/**
 * Represents {@link IEquipment} that can be held in hand.
 */
public interface IHandheldEquipment extends IDamageableEquipment {

    /**
     * The hit modifier of the underlying item.
     */
    int getHit();

    /**
     * The initiative modifier of the underlying item.
     */
    float getInitiative();
}
