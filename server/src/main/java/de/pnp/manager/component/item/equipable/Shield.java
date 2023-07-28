package de.pnp.manager.component.item.equipable;

import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.ItemType;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.item.interfaces.IDefensiveItem;
import jakarta.validation.constraints.Min;
import java.util.Objects;
import org.bson.types.ObjectId;

/**
 * A concrete shield in the universe.
 */
public class Shield extends HandheldEquipableItem implements IDefensiveItem {

    /**
     * The defense value of this armor.
     */
    @Min(0)
    protected final int armor;

    /**
     * The weight of this armor.
     */
    @Min(0)
    protected final float weight;

    public Shield(ObjectId id, String name, ItemType type, ItemType subtype, String requirement, String effect,
        ERarity rarity, int vendorPrice, int tier, String description, String note, Material material, int upgradeSlots,
        float initiative, int hit, float weight, int armor, int maximumStackSize, int minimumStackSize) {
        super(id, name, type, subtype, requirement, effect, rarity, vendorPrice, tier, description, note, material,
            upgradeSlots, initiative, hit, maximumStackSize, minimumStackSize);
        this.weight = weight;
        this.armor = armor;
    }

    public float getWeight() {
        return weight;
    }

    public int getArmor() {
        return armor;
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
        Shield that = (Shield) o;
        return Double.compare(that.getWeight(), getWeight()) == 0
            && getArmor() == that.getArmor();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getWeight(), getArmor());
    }
}
