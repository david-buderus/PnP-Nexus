package de.pnp.manager.component.item.equipable;

import de.pnp.manager.Tag;
import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.upgrade.effect.ItemEffect;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Set;

/**
 * A piece of jewellery in the universe.
 */
public class Jewellery extends EquipableItem {

    public Jewellery(ObjectId id, String name, Set<Tag> tags, String requirement, List<ItemEffect> effects, ERarity rarity,
                     int vendorPrice, int tier, String description, String note, Material material, int upgradeSlots,
                     int maximumStackSize, int minimumStackSize) {
        super(id, name, tags, requirement, effects, rarity, vendorPrice, tier, description, note, material,
                upgradeSlots, maximumStackSize, minimumStackSize);
    }
}
