package de.pnp.manager.component.inventory;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Floats;
import de.pnp.manager.component.crafting.bonus.UpgradeCraftingBonus.CraftingUpgrade;
import de.pnp.manager.component.inventory.equipment.DefensiveEquipment;
import de.pnp.manager.component.inventory.equipment.Equipment;
import de.pnp.manager.component.inventory.equipment.WeaponEquipment;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.interfaces.IItem;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.springframework.data.annotation.PersistenceCreator;

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
    private float stackSize;

    /**
     * The {@link Item} this {@link ItemStack} represents.
     */
    private final I item;

    /**
     * The {@link CraftingUpgrade boni} which this {@link ItemStack} got on its creation.
     */
    private final Collection<CraftingUpgrade> craftingUpgrades;

    public ItemStack(float stackSize, I item) {
        this(stackSize, item, new ArrayList<>());
    }

    @PersistenceCreator
    public ItemStack(float stackSize, I item, Collection<CraftingUpgrade> craftingUpgrades) {
        Preconditions.checkArgument(stackSize >= item.getMinimumStackSize() && stackSize <= item.getMaximumStackSize(),
            "The stackSize '%s' is forbidden for the item '%s.'", stackSize, item.getName());
        this.stackSize = stackSize;
        this.item = item;
        this.craftingUpgrades = craftingUpgrades;
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
     * The resulting amount is limited by {@link Item#getMinimumStackSize()} ()} and {@link Item#getMaximumStackSize()}
     * respectively.
     *
     * @return The resulting change in the amount of the {@link ItemStack}.
     */
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

    /**
     * Adds a {@link CraftingUpgrade} to this {@link ItemStack}.
     */
    public void addCraftingUpgrade(CraftingUpgrade upgrade) {
        craftingUpgrades.add(upgrade);
    }

    public Collection<CraftingUpgrade> getCraftingBonuses() {
        return Collections.unmodifiableCollection(craftingUpgrades);
    }
}
