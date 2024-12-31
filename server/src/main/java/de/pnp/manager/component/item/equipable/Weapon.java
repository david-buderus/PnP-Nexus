package de.pnp.manager.component.item.equipable;

import de.pnp.manager.component.Dice;
import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.item.interfaces.IOffensiveItem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.Objects;
import java.util.Set;
import org.bson.types.ObjectId;

/**
 * A concrete weapon in the universe.
 */
public class Weapon extends HandheldEquipableItem implements IOffensiveItem {

    /**
     * The damage modifier of this weapon.
     */
    @NotNull
    @PositiveOrZero
    protected final int damage;

    public Weapon(ObjectId id, String name, @NotNull Set<@NotBlank String> tags, String requirement, String effect,
        ERarity rarity,
        int vendorPrice, int tier, String description, String note, Material material, int upgradeSlots,
        float initiative, int hit, int damage, Dice dice, int maximumStackSize, int minimumStackSize) {
        super(id, name, tags, requirement, effect, rarity, vendorPrice, tier, description, note, material,
            upgradeSlots, initiative, hit, dice, maximumStackSize, minimumStackSize);
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
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
        Weapon that = (Weapon) o;
        return getDamage() == that.getDamage();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getDamage(), getDice());
    }
}
