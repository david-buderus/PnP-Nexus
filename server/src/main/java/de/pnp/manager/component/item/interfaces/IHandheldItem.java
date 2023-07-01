package de.pnp.manager.component.item.interfaces;

/**
 * An item that can be equipped in the hand.
 */
public interface IHandheldItem extends IEquipableItem {

    int getHit();

    float getInitiative();
}
