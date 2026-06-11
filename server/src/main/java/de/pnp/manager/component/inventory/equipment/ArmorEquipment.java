package de.pnp.manager.component.inventory.equipment;

import de.pnp.manager.component.item.equipable.Armor;
import de.pnp.manager.component.item.equipable.EArmorSlot;

/**
 * Represents an {@link Armor} that can be held and used.
 */
public class ArmorEquipment extends DefensiveEquipment<Armor> {

    public ArmorEquipment(float stackSize, Armor item, float wear) {
        super(stackSize, item, wear);
    }

    /**
     * @see Armor#getArmorSlot()
     */
    public EArmorSlot getArmorSlot() {
        return getItem().getArmorSlot();
    }
}
