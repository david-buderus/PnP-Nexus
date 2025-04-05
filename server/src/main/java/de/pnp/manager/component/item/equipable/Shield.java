package de.pnp.manager.component.item.equipable;

import de.pnp.manager.Tag;
import de.pnp.manager.component.Dice;
import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.item.interfaces.IDefensiveItem;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.Objects;
import java.util.Set;
import org.bson.types.ObjectId;

/**
 * A concrete shield in the universe.
 */
public class Shield extends HandheldEquipableItem implements IDefensiveItem {

    /**
     * The defense value of this shield.
     */
    @NotNull
    @PositiveOrZero
    protected final int armor;

    /**
     * The protection value of this shield.
     */
    @NotNull
    @PositiveOrZero
    protected final int protection;

    /**
     * The weight of this shield.
     */
    @NotNull
    @PositiveOrZero
    protected final float weight;

    public Shield(ObjectId id, String name, Set<@NotNull Tag> tags, String requirement, String effect,
        ERarity rarity,
        int vendorPrice, int tier, String description, String note, Material material, int upgradeSlots,
        float initiative, int hit, Dice dice, float weight, int armor, int protection,
        int maximumStackSize, int minimumStackSize) {
        super(id, name, tags, requirement, effect, rarity, vendorPrice, tier, description, note, material,
            upgradeSlots, initiative, hit, dice, maximumStackSize, minimumStackSize);
        this.weight = weight;
        this.armor = armor;
        this.protection = protection;
    }

    public float getWeight() {
        return weight;
    }

    public int getArmor() {
        return armor;
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
        Shield that = (Shield) o;
        return Double.compare(that.getWeight(), getWeight()) == 0
            && getArmor() == that.getArmor() && getProtection() == that.getProtection();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getWeight(), getArmor(), getProtection());
    }
}
