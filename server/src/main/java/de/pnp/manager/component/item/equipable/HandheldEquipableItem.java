package de.pnp.manager.component.item.equipable;

import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.ItemType;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.item.interfaces.IHandheldItem;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import org.bson.types.ObjectId;

/**
 * A concrete item that can be equipped in the hand.
 */
public abstract class HandheldEquipableItem extends EquipableItem implements IHandheldItem {

    /**
     * The initiative modifier of this item.
     */
    @NotNull
    protected final float initiative;

    /**
     * The hit modifier of this item.
     */
    @NotNull
    protected final int hit;

    public HandheldEquipableItem(ObjectId id, String name, ItemType type, ItemType subtype, String requirement,
        String effect, ERarity rarity, int vendorPrice, int tier, String description, String note, Material material,
        int upgradeSlots, float initiative, int hit, int maximumStackSize, int minimumStackSize) {
        super(id, name, type, subtype, requirement, effect, rarity, vendorPrice, tier, description, note, material,
            upgradeSlots, maximumStackSize, minimumStackSize);
        this.initiative = initiative;
        this.hit = hit;
    }

    public float getInitiative() {
        return initiative;
    }

    public int getHit() {
        return hit;
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
        HandheldEquipableItem that = (HandheldEquipableItem) o;
        return Float.compare(that.getInitiative(), getInitiative()) == 0
            && getHit() == that.getHit();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getInitiative(), getHit());
    }
}
