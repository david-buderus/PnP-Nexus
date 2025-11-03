package de.pnp.manager.component.item.interfaces;

import de.pnp.manager.component.Dice;

/**
 * An item that can be equipped in the hand.
 */
public interface IHandheldItem extends IEquipableItem {
    
    /**
     * The dice used while attacking with this item.
     */
    Dice getDice();

    /**
     * The hit of the underlying item.
     */
    int getHit();

    /**
     * The init of the underlying item.
     */
    float getInitiative();
}
