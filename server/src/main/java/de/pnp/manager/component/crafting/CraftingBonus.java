package de.pnp.manager.component.crafting;

import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.component.crafting.bonus.ICraftingBonusEffect;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.ItemType;
import java.util.Objects;
import org.bson.types.ObjectId;

/**
 * A bonus an {@link Item} can get if it is crafted by a player.
 */
public class CraftingBonus extends DatabaseObject {

    /**
     * The name of this bonus.
     */
    private final String name;

    /**
     * The {@link ItemType} of the {@link Item} where this bonus can be applied.
     */
    private final ItemType target;

    /**
     * The effect of this bonus.
     */
    private final ICraftingBonusEffect effect;

    protected CraftingBonus(ObjectId id, String name, ItemType target, ICraftingBonusEffect effect) {
        super(id);
        this.name = name;
        this.target = target;
        this.effect = effect;
    }

    public String getName() {
        return name;
    }

    public ItemType getTarget() {
        return target;
    }

    public ICraftingBonusEffect getEffect() {
        return effect;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CraftingBonus that = (CraftingBonus) o;
        return Objects.equals(getName(), that.getName()) && Objects.equals(getTarget(),
            that.getTarget()) && Objects.equals(getEffect(), that.getEffect());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getTarget(), getEffect());
    }
}
