package de.pnp.manager.component.inventory.equipment.interfaces;

/**
 * Represents equipment that can be damaged.
 */
public interface IDamageableEquipment {

    /**
     * Applies wear to the equipment.
     */
    void applyWear(float wear);

    /**
     * Completely repairs the equipment.
     */
    void repair();

    /**
     * Returns a float [0,1] representing the current durability in percent.
     */
    float getRelativeDurability();
}
