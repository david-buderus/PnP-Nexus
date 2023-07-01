package de.pnp.manager.component.item.equipable;

import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.ItemType;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.item.interfaces.IEquipableItem;
import java.util.Objects;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * A concrete item that can be equipped.
 */
public abstract class EquipableItem extends Item implements IEquipableItem {

    /**
     * The {@link Material} of this item.
     */
    @DBRef
    protected final Material material;

    /**
     * The amount of upgrades this item can hold.
     */
    protected final int upgradeSlots;

    public EquipableItem(ObjectId id, String name, ItemType type, ItemType subtype,
        String requirement,
        String effect,
        ERarity rarity, int vendorPrice, int tier, String description,
        String note, Material material, int upgradeSlots) {
        super(id, name, type, subtype, requirement, effect, rarity, vendorPrice, tier, description,
            note);
        this.material = material;
        this.upgradeSlots = upgradeSlots;
    }

    public Material getMaterial() {
        return material;
    }

    public int getUpgradeSlots() {
        return upgradeSlots;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        EquipableItem that = (EquipableItem) o;
        return getUpgradeSlots() == that.getUpgradeSlots() && Objects.equals(getMaterial(),
            that.getMaterial());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getMaterial(), getUpgradeSlots());
    }
}
