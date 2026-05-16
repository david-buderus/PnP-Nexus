package de.pnp.manager.component.inventory.equipment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.base.Preconditions;
import com.google.common.collect.Streams;
import de.pnp.manager.component.ECalculation;
import de.pnp.manager.component.inventory.ItemStack;
import de.pnp.manager.component.inventory.equipment.interfaces.IEquipment;
import de.pnp.manager.component.item.equipable.EquipableItem;
import de.pnp.manager.component.item.interfaces.IEquipableItem;
import de.pnp.manager.component.upgrade.Upgrade;
import de.pnp.manager.component.upgrade.effect.EItemEquipmentManipulator;
import de.pnp.manager.component.upgrade.effect.EquipmentItemEffect;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Represents an {@link EquipableItem} that can be held and used.
 */
@JsonSubTypes({
        @JsonSubTypes.Type(value = Equipment.class, name = "Equipment"),
        @JsonSubTypes.Type(value = ShieldEquipment.class, name = "ShieldEquipment"),
        @JsonSubTypes.Type(value = ArmorEquipment.class, name = "ArmorEquipment"),
        @JsonSubTypes.Type(value = WeaponEquipment.class, name = "WeaponEquipment"),
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
public class Equipment<E extends IEquipableItem> extends ItemStack<E> implements IEquipment {

    /**
     * The {@link Upgrade upgrades} of the {@link EquipableItem}.
     */
    @DBRef
    @NotNull
    private Collection<Upgrade> upgrades;

    public Equipment(float amount, E item) {
        super(amount, item);
        upgrades = new ArrayList<>();
    }

    @JsonCreator
    public Equipment(float stackSize, E item, Collection<Upgrade> upgrades) {
        super(stackSize, item);
        this.upgrades = upgrades;
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
     * @see #upgrades
     */
    public Collection<Upgrade> getUpgrades() {
        return Collections.unmodifiableCollection(upgrades);
    }

    /**
     * Sets the {@link #upgrades} of the equipment.
     */
    public void setUpgrades(Collection<Upgrade> upgrades) {
        int requiredSlots = upgrades.stream().mapToInt(Upgrade::getSlots).sum();
        Preconditions.checkArgument(requiredSlots <= getUpgradeSlots(),
                "The required '%s' slots of the upgrades exceed the capacity of the item '%s'.",
                requiredSlots, getItem().getName());
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
}
