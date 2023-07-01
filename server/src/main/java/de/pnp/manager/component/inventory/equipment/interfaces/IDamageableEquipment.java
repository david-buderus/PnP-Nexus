package de.pnp.manager.component.inventory.equipment.interfaces;

public interface IDamageableEquipment extends IEquipment {

    void applyWear(int wear);

    void repair();

    /**
     * Returns a float [0,1] representing the current durability in percent.
     *
     * @return
     */
    float getRelativeDurability();
}
