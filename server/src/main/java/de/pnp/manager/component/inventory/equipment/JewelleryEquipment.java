package de.pnp.manager.component.inventory.equipment;

import de.pnp.manager.component.inventory.ItemStack;
import de.pnp.manager.component.item.equipable.Jewellery;

/**
 * Represents an {@link Jewellery} that can be held and used.
 */
public class JewelleryEquipment extends ItemStack<Jewellery> {

    public JewelleryEquipment(float stackSize, Jewellery item) {
        super(stackSize, item);
    }
}
