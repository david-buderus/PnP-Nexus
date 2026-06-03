package de.pnp.manager.component.inventory.equipment;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.pnp.manager.component.inventory.ItemStack;
import de.pnp.manager.component.inventory.equipment.interfaces.IDamageableEquipment;
import de.pnp.manager.component.item.equipable.EquipableItem;
import de.pnp.manager.component.item.interfaces.IDamageableItem;
import de.pnp.manager.component.item.interfaces.IEquipableItem;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.Objects;

/**
 * Represents an {@link IDamageableItem} that can be held and used.
 */
public abstract class DamageableEquipment<I extends EquipableItem & IDamageableItem> extends
        ItemStack<I> implements IDamageableEquipment {

    /**
     * The current wear of this {@link IEquipableItem}.
     */
    @PositiveOrZero
    @JsonProperty("wear")
    protected float wear;

    protected DamageableEquipment(float stackSize, I item, float wear) {
        super(stackSize, item);
        this.wear = wear;
    }

    @Override
    public void applyWear(float wear) {
        this.wear += wear;
    }

    @Override
    public void repair() {
        wear = 0;
    }

    @Override
    public float getRelativeDurability() {
        if (getMaxDurability() == 0) {
            return 1 - wear;
        }
        return Math.max(0, (getMaxDurability() - wear) / getMaxDurability());
    }

    /**
     * Returns the maximal durability.
     */
    public abstract int getMaxDurability();


    @Override
    public boolean canStack(ItemStack<?> other) {
        if (!super.canStack(other)) {
            return false;
        }
        return Float.compare(wear, ((DamageableEquipment<?>) other).wear) == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        DamageableEquipment<?> that = (DamageableEquipment<?>) o;
        return Float.compare(wear, that.wear) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), wear);
    }
}
