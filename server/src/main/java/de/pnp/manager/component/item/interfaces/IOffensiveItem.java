package de.pnp.manager.component.item.interfaces;

/**
 * An item that can be used to deal damage.
 */
public interface IOffensiveItem extends IDamageableItem, IHandheldItem {

    /**
     * The dice used while attacking with this item.
     */
    String getDice();

    /**
     * The flat damage of the item.
     */
    int getDamage();
}
