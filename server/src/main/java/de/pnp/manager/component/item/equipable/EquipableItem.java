package de.pnp.manager.component.item.equipable;

import de.pnp.manager.Tag;
import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.item.interfaces.IEquipableItem;
import de.pnp.manager.component.upgrade.effect.ItemEffect;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A concrete item that can be equipped.
 */
public abstract class EquipableItem extends Item implements IEquipableItem {

    /**
     * The {@link Material} of this item.
     */
    @DBRef
    @NotNull
    protected final Material material;

    /**
     * The amount of upgrades this item can hold.
     */
    @NotNull
    @PositiveOrZero
    protected final int upgradeSlots;

    public EquipableItem(ObjectId id, String name, Set<Tag> tags, String requirement, List<ItemEffect> effects,
                         ERarity rarity, int vendorPrice, int tier, String description, String note, Material material,
                         int upgradeSlots, int maximumStackSize, int minimumStackSize) {
        super(id, name, tags, requirement, effects, rarity, vendorPrice, tier, description,
                note, maximumStackSize, minimumStackSize);
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
