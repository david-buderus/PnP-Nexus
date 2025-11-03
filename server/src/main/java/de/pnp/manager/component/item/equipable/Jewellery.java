package de.pnp.manager.component.item.equipable;

import de.pnp.manager.Tag;
import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.Material;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import org.bson.types.ObjectId;

/**
 * A piece of jewellery in the universe.
 */
public class Jewellery extends EquipableItem {

    public Jewellery(ObjectId id, String name, Set<@NotNull Tag> tags, String requirement, String effect,
        ERarity rarity,
        int vendorPrice, int tier, String description, String note, Material material, int upgradeSlots,
        int maximumStackSize, int minimumStackSize) {
        super(id, name, tags, requirement, effect, rarity, vendorPrice, tier, description, note, material,
            upgradeSlots, maximumStackSize, minimumStackSize);
    }
}
