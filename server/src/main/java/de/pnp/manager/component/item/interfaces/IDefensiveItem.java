package de.pnp.manager.component.item.interfaces;

/**
 * An item that can be used for defense.
 */
public interface IDefensiveItem extends IDamageableItem, IEquipableItem {

    /**
     * The weight of the item.
     */
    float getWeight();

    /**
     * The armor of the item.
     */
    int getArmor();

    /**
     * The protection of the item.
     */
    int getProtection();
}
