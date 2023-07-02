package de.pnp.manager.component.inventory;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Floats;
import de.pnp.manager.component.inventory.equipment.DefensiveEquipment;
import de.pnp.manager.component.inventory.equipment.Equipment;
import de.pnp.manager.component.inventory.equipment.WeaponEquipment;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.interfaces.IItem;

/**
 * Represents an {@link Item} that can be held and used.
 */
@JsonSubTypes({
    @JsonSubTypes.Type(value = Equipment.class),
    @JsonSubTypes.Type(value = DefensiveEquipment.class),
    @JsonSubTypes.Type(value = WeaponEquipment.class),
})
public class ItemStack<I extends IItem> {

    /**
     * The amount of the {@link #item} this {@link ItemStack} holds.
     */
    private float amount;

    /**
     * The {@link Item} this {@link ItemStack} represents.
     */
    private final I item;

    public ItemStack(float amount, I item) {
        Preconditions.checkArgument(amount >= item.getMinimumStackSize() && amount <= item.getMaximumStackSize(),
            "The stackSize %s is forbidden for the item %s.", amount, item.getName());
        this.amount = amount;
        this.item = item;
    }

    /**
     * Adds the given amount of this {@link ItemStack}.
     * <p>
     * The resulting amount is limited by {@link Item#getMinimumStackSize()} ()} and {@link Item#getMaximumStackSize()}
     * respectively.
     *
     * @return The resulting change in the amount of the {@link ItemStack}.
     */
    public float addAmount(float amount) {
        return setAmount(getAmount() + amount);
    }

    /**
     * Subtracts the given amount of this {@link ItemStack}.
     * <p>
     * The resulting amount is limited by {@link Item#getMinimumStackSize()} ()} and {@link Item#getMaximumStackSize()}
     * respectively.
     *
     * @return The resulting change in the amount of the {@link ItemStack}.
     */
    public float subtractAmount(float amount) {
        return setAmount(getAmount() - amount);
    }

    /**
     * Sets the amount of this {@link ItemStack}.
     * <p>
     * The resulting amount is limited by {@link Item#getMinimumStackSize()} ()} and {@link Item#getMaximumStackSize()}
     * respectively.
     *
     * @return The resulting change in the amount of the {@link ItemStack}.
     */
    public float setAmount(float amount) {
        float newAmount = Floats.constrainToRange(amount, item.getMinimumStackSize(),
            item.getMaximumStackSize());
        float change = newAmount - getAmount();
        this.amount = newAmount;
        return change;
    }

    public float getAmount() {
        return amount;
    }

    public I getItem() {
        return item;
    }
}
