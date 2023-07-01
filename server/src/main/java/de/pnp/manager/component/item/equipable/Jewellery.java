package de.pnp.manager.component.item.equipable;

import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.ItemType;
import de.pnp.manager.component.item.Material;
import org.bson.types.ObjectId;

/**
 * A piece of jewellery in the universe.
 */
public class Jewellery extends EquipableItem {

    public Jewellery(ObjectId id, String name, ItemType type, ItemType subtype, String requirement, String effect,
        ERarity rarity, int vendorPrice, int tier, String description, String note, Material material, int upgradeSlots,
        int maximalStackSize, int minimalStackSize) {
        super(id, name, type, subtype, requirement, effect, rarity, vendorPrice, tier, description, note, material,
            upgradeSlots, maximalStackSize, minimalStackSize);
    }
}
