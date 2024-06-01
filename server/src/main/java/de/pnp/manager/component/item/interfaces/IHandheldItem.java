package de.pnp.manager.component.item.interfaces;

/**
 * An item that can be equipped in the hand.
 */
public interface IHandheldItem extends IEquipableItem {

    /**
     * The hit of the underlying item.
     */
    int getHit();

    /**
     * The init of the underlying item.
     */
    float getInitiative();
}
