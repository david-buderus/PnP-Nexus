package de.pnp.manager.component.item.interfaces;

/**
 * An item that can be equipped.
 */
public interface IEquipableItem extends IItem {

    /**
     * The number of upgrade slots of the underlying item.
     */
    int getUpgradeSlots();
}
