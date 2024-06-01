package de.pnp.manager.component.inventory.equipment;

import de.pnp.manager.component.item.equipable.Armor;

/**
 * Represents an {@link Armor} that can be held and used.
 */
public class ArmorEquipment extends DefensiveEquipment<Armor> {

    public ArmorEquipment(float stackSize, Armor item, int wear) {
        super(stackSize, item, wear);
    }
}
