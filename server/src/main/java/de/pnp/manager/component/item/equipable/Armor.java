package de.pnp.manager.component.item.equipable;

import de.pnp.manager.Tag;
import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.item.interfaces.IDefensiveItem;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.Objects;
import java.util.Set;
import org.bson.types.ObjectId;

/**
 * Armor in the universe.
 */
public class Armor extends EquipableItem implements IDefensiveItem {

    @NotNull
    protected final EArmorSlot armorSlot;

    /**
     * The defense value of this armor.
     */
    @NotNull
    @PositiveOrZero
    protected final int armor;

    /**
     * The protection value of this armor.
     */
    @NotNull
    @PositiveOrZero
    protected final int protection;

    /**
     * The weight of this armor.
     */
    @NotNull
    @PositiveOrZero
    protected final float weight;

    public Armor(ObjectId id, String name, Set<@NotNull Tag> tags, String requirement, String effect,
        ERarity rarity, int vendorPrice, int tier, String description, String note, Material material, int upgradeSlots,
        EArmorSlot armorSlot, int armor, int protection, float weight, int maximumStackSize, int minimumStackSize) {
        super(id, name, tags, requirement, effect, rarity, vendorPrice, tier, description, note, material,
            upgradeSlots, maximumStackSize, minimumStackSize);
        this.armorSlot = armorSlot;
        this.armor = armor;
        this.protection = protection;
        this.weight = weight;
    }

    public int getArmor() {
        return armor;
    }

    public float getWeight() {
        return weight;
    }

    public EArmorSlot getArmorSlot() {
        return armorSlot;
    }

    public int getProtection() {
        return protection;
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
        Armor armor1 = (Armor) o;
        return getArmor() == armor1.getArmor() && getProtection() == armor1.getProtection()
            && Float.compare(armor1.getWeight(), getWeight()) == 0 && getArmorSlot() == armor1.getArmorSlot();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getArmorSlot(), getArmor(), getProtection(), getWeight());
    }
}
