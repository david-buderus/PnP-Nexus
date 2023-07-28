package de.pnp.manager.component.item.equipable;

import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.ItemType;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.item.interfaces.IOffensiveItem;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;
import org.bson.types.ObjectId;

/**
 * A concrete weapon in the universe.
 */
public class Weapon extends HandheldEquipableItem implements IOffensiveItem {

    /**
     * The damage modifier of this weapon.
     */
    @Min(0)
    protected final int damage;

    /**
     * The dice to determine the damage.
     */
    @NotBlank
    protected final String dice;

    public Weapon(ObjectId id, String name, ItemType type, ItemType subtype, String requirement,
        String effect,
        ERarity rarity, int vendorPrice, int tier, String description, String note,
        Material material,
        int upgradeSlots, float initiative, int hit, int damage, String dice) {
        super(id, name, type, subtype, requirement, effect, rarity, vendorPrice, tier, description,
            note,
            material, upgradeSlots, initiative, hit);
        this.damage = damage;
        this.dice = dice;
    }

    public int getDamage() {
        return damage;
    }

    public String getDice() {
        return dice;
    }

    @Override
    public int getMaxDurability() {
        return getDamage();
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
        return getDamage() == that.getDamage() && Objects.equals(getDice(), that.getDice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getDamage(), getDice());
    }
}
