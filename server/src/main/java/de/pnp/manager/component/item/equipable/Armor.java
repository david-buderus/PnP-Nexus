package de.pnp.manager.component.item.equipable;

import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.ItemType;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.item.interfaces.IDefensiveItem;
import java.util.Objects;
import org.bson.types.ObjectId;

/**
 * Armor in the universe.
 */
public class Armor extends EquipableItem implements IDefensiveItem {

    /**
     * The defense value of this armor.
     */
    protected final int armor;

    /**
     * The weight of this armor.
     */
    protected final float weight;

    public Armor(ObjectId id, String name, ItemType type, ItemType subtype, String requirement, String effect,
        ERarity rarity, int vendorPrice, int tier, String description, String note, Material material, int upgradeSlots,
        int armor, float weight, int maximumStackSize, int minimumStackSize) {
        super(id, name, type, subtype, requirement, effect, rarity, vendorPrice, tier, description, note, material,
            upgradeSlots, maximumStackSize, minimumStackSize);
        this.armor = armor;
        this.weight = weight;
    }

    public int getArmor() {
        return armor;
    }

    public float getWeight() {
        return weight;
    }

    @Override
    public int getMaxDurability() {
        return getArmor();
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
        Armor that = (Armor) o;
        return getArmor() == that.getArmor()
            && Double.compare(that.getWeight(), getWeight()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getArmor(), getWeight());
    }
}
