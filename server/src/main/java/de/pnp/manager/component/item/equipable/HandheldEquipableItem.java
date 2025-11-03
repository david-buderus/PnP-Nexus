package de.pnp.manager.component.item.equipable;

import de.pnp.manager.Tag;
import de.pnp.manager.component.Dice;
import de.pnp.manager.component.item.ERarity;
import de.pnp.manager.component.item.Material;
import de.pnp.manager.component.item.interfaces.IHandheldItem;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;
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
    /**
     * The dice to determine the damage.
     */
    @Valid
    @NotNull
    protected final Dice dice;

    public HandheldEquipableItem(ObjectId id, String name, Set<@NotNull Tag> tags, String requirement,
        String effect, ERarity rarity, int vendorPrice, int tier, String description, String note, Material material,
        int upgradeSlots, float initiative, int hit, Dice dice, int maximumStackSize, int minimumStackSize) {
        super(id, name, tags, requirement, effect, rarity, vendorPrice, tier, description, note, material,
            upgradeSlots, maximumStackSize, minimumStackSize);
        this.initiative = initiative;
        this.hit = hit;
        this.dice = dice;
    }

    public float getInitiative() {
        return initiative;
    }

    public int getHit() {
        return hit;
    }

    public Dice getDice() {
        return dice;
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
            && getHit() == that.getHit() && Objects.equals(getDice(), that.getDice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getInitiative(), getHit(), getDice());
    }
}
