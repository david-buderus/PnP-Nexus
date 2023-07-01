package de.pnp.manager.component.inventory;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import de.pnp.manager.component.inventory.equipment.DefensiveEquipment;
import de.pnp.manager.component.inventory.equipment.Equipment;
import de.pnp.manager.component.inventory.equipment.WeaponEquipment;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.interfaces.IItem;

/**
 * Represents an {@link Item} that can be hold and used.
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
        this.amount = amount;
        this.item = item;
    }

    /**
     * Adds the given amount of this {@link ItemStack}.
     * <p>
     * The resulting amount is clamped by {@link #getMinAmount()} and {@link #getMaxAmount()}.
     *
     * @return The resulting change in the amount of the {@link ItemStack}.
     */
    public float addAmount(float amount) {
        return setAmount(getAmount() + amount);
    }

    /**
     * Subtracts the given amount of this {@link ItemStack}.
     * <p>
     * The resulting amount is clamped by {@link #getMinAmount()} and {@link #getMaxAmount()}.
     *
     * @return The resulting change in the amount of the {@link ItemStack}.
     */
    public float subtractAmount(float amount) {
        return setAmount(getAmount() - amount);
    }

    /**
     * Sets the amount of this {@link ItemStack}.
     * <p>
     * The resulting amount is clamped by {@link #getMinAmount()} and {@link #getMaxAmount()}.
     *
     * @return The resulting change in the amount of the {@link ItemStack}.
     */
    public float setAmount(float amount) {
        float newAmount = Math.max(Math.min(getAmount() + amount, getMaxAmount()), getMinAmount());
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

    /**
     * Returns the maximal amount the {@link ItemStack} can hold.
     */
    public float getMaxAmount() {
        return 100;
    }

    /**
     * Returns the minimum amount the {@link ItemStack} needs to hold.
     */
    public float getMinAmount() {
        return 0;
    }
}
