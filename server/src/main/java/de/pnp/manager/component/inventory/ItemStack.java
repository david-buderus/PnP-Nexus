package de.pnp.manager.component.inventory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Floats;
import de.pnp.manager.component.inventory.equipment.ArmorEquipment;
import de.pnp.manager.component.inventory.equipment.Equipment;
import de.pnp.manager.component.inventory.equipment.ShieldEquipment;
import de.pnp.manager.component.inventory.equipment.WeaponEquipment;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.interfaces.IItem;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * Represents an {@link Item} that can be held and used.
 */
@JsonSubTypes({
        @JsonSubTypes.Type(value = Equipment.class, name = "Equipment"),
        @JsonSubTypes.Type(value = ShieldEquipment.class, name = "ShieldEquipment"),
        @JsonSubTypes.Type(value = ArmorEquipment.class, name = "ArmorEquipment"),
        @JsonSubTypes.Type(value = WeaponEquipment.class, name = "WeaponEquipment"),
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
public class ItemStack<I extends IItem> {

    /**
     * The amount of the {@link #item} this {@link ItemStack} holds.
     */
    @PositiveOrZero
    private float stackSize;

    /**
     * The {@link Item} this {@link ItemStack} represents.
     */
    @NotNull
    @DBRef
    private final I item;

    @JsonCreator
    public ItemStack(float stackSize, I item) {
        Preconditions.checkArgument(stackSize >= item.getMinimumStackSize() && stackSize <= item.getMaximumStackSize(),
                "The stackSize '%s' is forbidden for the item '%s.'", stackSize, item.getName());
        this.stackSize = stackSize;
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
        return setAmount(getStackSize() + amount);
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
        return setAmount(getStackSize() - amount);
    }

    /**
     * Sets the amount of this {@link ItemStack}.
     * <p>
     * The resulting amount is limited by {@link Item#getMinimumStackSize()} and {@link Item#getMaximumStackSize()}
     * respectively.
     *
     * @return The resulting change in the amount of the {@link ItemStack}.
     */
    @JsonIgnore
    public float setAmount(float amount) {
        float newAmount = Floats.constrainToRange(amount, item.getMinimumStackSize(),
                item.getMaximumStackSize());
        float change = newAmount - getStackSize();
        this.stackSize = newAmount;
        return change;
    }

    public float getStackSize() {
        return stackSize;
    }

    public I getItem() {
        return item;
    }
}
