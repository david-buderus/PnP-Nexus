package de.pnp.manager.component.upgrade;

import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.component.item.ItemType;
import de.pnp.manager.component.item.equipable.EquipableItem;
import de.pnp.manager.component.upgrade.effect.IUpgradeEffect;
import java.util.Collection;
import java.util.Objects;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * A concrete upgrade of an {@link EquipableItem} in a universe.
 */
public class Upgrade extends DatabaseObject {

    /**
     * The name of this upgrade.
     */
    private final String name;

    /**
     * The {@link ItemType} on which this {@link Upgrade} can be used.
     */
    @DBRef
    private final ItemType target;

    /**
     * The amount of {@link EquipableItem#getUpgradeSlots() slots} needed for an {@link EquipableItem} to hold this
     * upgrade.
     */
    private final int slots;

    /**
     * The average price of this item.
     */
    protected final int vendorPrice;

    /**
     * The {@link IUpgradeEffect effects} oft this upgrade.
     */
    private final Collection<IUpgradeEffect> effects;

    public Upgrade(ObjectId id, String name, ItemType target, int slots, int vendorPrice,
        Collection<IUpgradeEffect> effects) {
        super(id);
        this.name = name;
        this.target = target;
        this.slots = slots;
        this.vendorPrice = vendorPrice;
        this.effects = effects;
    }

    public String getName() {
        return name;
    }

    public ItemType getTarget() {
        return target;
    }

    public int getSlots() {
        return slots;
    }

    public int getVendorPrice() {
        return vendorPrice;
    }

    public Collection<IUpgradeEffect> getEffects() {
        return effects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Upgrade upgrade = (Upgrade) o;
        return getSlots() == upgrade.getSlots() && getVendorPrice() == upgrade.getVendorPrice() && Objects.equals(
            getName(), upgrade.getName()) && Objects.equals(getTarget(), upgrade.getTarget())
            && Objects.equals(getEffects(), upgrade.getEffects());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getTarget(), getSlots(), getVendorPrice(), getEffects());
    }
}
