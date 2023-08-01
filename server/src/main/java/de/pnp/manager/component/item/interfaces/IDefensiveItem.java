package de.pnp.manager.component.item.interfaces;

/**
 * An item that can be used for defense.
 */
public interface IDefensiveItem extends IDamageableItem, IEquipableItem {

    float getWeight();

    int getArmor();
}
