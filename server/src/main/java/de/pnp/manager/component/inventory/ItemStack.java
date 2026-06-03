package de.pnp.manager.component.inventory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Streams;
import com.google.common.primitives.Floats;
import de.pnp.manager.component.ECalculation;
import de.pnp.manager.component.inventory.equipment.ArmorEquipment;
import de.pnp.manager.component.inventory.equipment.JewelleryEquipment;
import de.pnp.manager.component.inventory.equipment.ShieldEquipment;
import de.pnp.manager.component.inventory.equipment.WeaponEquipment;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.equipable.*;
import de.pnp.manager.component.upgrade.Upgrade;
import de.pnp.manager.component.upgrade.effect.EItemEquipmentManipulator;
import de.pnp.manager.component.upgrade.effect.EquipmentItemEffect;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.*;

/**
 * Represents an {@link Item} that can be held and used.
 */
@JsonSubTypes({
        @JsonSubTypes.Type(value = ItemStack.class, name = "ItemStack"),
        @JsonSubTypes.Type(value = ShieldEquipment.class, name = "ShieldEquipment"),
        @JsonSubTypes.Type(value = ArmorEquipment.class, name = "ArmorEquipment"),
        @JsonSubTypes.Type(value = WeaponEquipment.class, name = "WeaponEquipment"),
        @JsonSubTypes.Type(value = JewelleryEquipment.class, name = "JewelleryEquipment"),
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
public class ItemStack<I extends Item> implements Cloneable {

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

    /**
     * The {@link Upgrade upgrades} of the {@link Item}.
     */
    @DBRef
    @NotNull
    private Collection<Upgrade> upgrades;

    public ItemStack(float stackSize, I item) {
        this(stackSize, item, new ArrayList<>());
    }

    @JsonCreator
    public ItemStack(float stackSize, I item, Collection<Upgrade> upgrades) {
        this.stackSize = stackSize;
        this.item = item;
        this.upgrades = upgrades;
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

    /**
     * Returns {@link EquipableItem#getUpgradeSlots()} in regard to the {@link #upgrades}.
     */
    public int getUpgradeSlots() {
        return applyItemEffects(EItemEquipmentManipulator.SLOTS, getItem().getUpgradeSlots());
    }

    /**
     * Returns the amount of {@link EquipableItem#getUpgradeSlots() upgrade slots} which are not in use.
     */
    public int getRemainingUpgradeSlots() {
        return getUpgradeSlots() - getUpgrades().stream().mapToInt(Upgrade::getSlots).sum();
    }

    /**
     * Sets the {@link #upgrades} of the equipment.
     */
    public void setUpgrades(Collection<Upgrade> upgrades) {
        this.upgrades = upgrades;
    }

    /**
     * Adds an {@link Upgrade} to the equipment.
     */
    public void addUpgrade(Upgrade upgrade) {
        Preconditions.checkArgument(upgrade.getSlots() <= getRemainingUpgradeSlots(),
                "The required '%s' slots of the upgrades exceed the capacity of the item '%s'.",
                upgrade.getSlots() + getUpgradeSlots(), getItem().getName());
        upgrades.add(upgrade);
    }

    /**
     * Removes an {@link Upgrade} from the equipment.
     */
    public void removeUpgrade(Upgrade upgrade) {
        upgrades.remove(upgrade);
    }

    /**
     * Applies the effects of the {@link #upgrades} to the value.
     */
    protected int applyItemEffects(EItemEquipmentManipulator manipulator, int value) {
        return Math.round(applyItemEffects(manipulator, (float) value));
    }

    /**
     * Applies the effects of the {@link #upgrades} to the value.
     */
    protected float applyItemEffects(EItemEquipmentManipulator manipulator, float value) {
        List<EquipmentItemEffect> effects = Streams.concat(
                getItem().getEffects().stream(),
                getUpgrades().stream().flatMap(upgrade -> upgrade.getEffects().stream())
        ).filter(EquipmentItemEffect.class::isInstance).map(EquipmentItemEffect.class::cast).toList();

        List<EquipmentItemEffect> additiveEffects = effects.stream()
                .filter(effect -> effect.getCalculation() == ECalculation.ADDITIVE)
                .toList();
        List<EquipmentItemEffect> multiplicativeEffects = effects.stream()
                .filter(effect -> effect.getCalculation() == ECalculation.MULTIPLICATIVE)
                .toList();

        for (EquipmentItemEffect effect : additiveEffects) {
            value = effect.apply(manipulator, value);
        }
        for (EquipmentItemEffect effect : multiplicativeEffects) {
            value = effect.apply(manipulator, value);
        }

        return value;
    }

    public float getStackSize() {
        return stackSize;
    }

    public I getItem() {
        return item;
    }

    public Collection<Upgrade> getUpgrades() {
        return Collections.unmodifiableCollection(upgrades);
    }

    /**
     * Returns if the other item stack could be stacked onto this one.
     * This ignores size limits and only checks if the stacks are theoretically compatible.
     */
    public boolean canStack(ItemStack<?> other) {
        if (other == null || other.getClass() != this.getClass()) {
            return false;
        }
        return Objects.equals(item, other.getItem()) && Objects.equals(upgrades, other.upgrades);
    }

    /**
     * Creates the matching {@link ItemStack} from the given {@link Item}
     */
    public static ItemStack<? extends Item> from(Item item, float stackSize) {
        if (item instanceof Weapon weapon) {
            return new WeaponEquipment(stackSize, weapon, 0);
        }
        if (item instanceof Shield shield) {
            return new ShieldEquipment(stackSize, shield, 0);
        }
        if (item instanceof Armor armor) {
            return new ArmorEquipment(stackSize, armor, 0);
        }
        if (item instanceof Jewellery jewellery) {
            return new JewelleryEquipment(stackSize, jewellery);
        }
        return new ItemStack<>(stackSize, item);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ItemStack<?> itemStack = (ItemStack<?>) o;
        return Float.compare(stackSize, itemStack.stackSize) == 0
                && Objects.equals(item, itemStack.item)
                && Objects.equals(upgrades, itemStack.upgrades);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stackSize, item, upgrades);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ItemStack<I> clone() {
        try {
            ItemStack<I> clone = (ItemStack<I>) super.clone();
            clone.setUpgrades(new ArrayList<>(upgrades));
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("stackSize", stackSize)
                .add("item", item)
                .add("upgrades", upgrades)
                .toString();
    }
}
