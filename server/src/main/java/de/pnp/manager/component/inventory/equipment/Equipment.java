package de.pnp.manager.component.inventory.equipment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.base.Preconditions;
import de.pnp.manager.component.ECalculation;
import de.pnp.manager.component.inventory.ItemStack;
import de.pnp.manager.component.inventory.equipment.interfaces.IEquipment;
import de.pnp.manager.component.item.equipable.EquipableItem;
import de.pnp.manager.component.item.interfaces.IEquipableItem;
import de.pnp.manager.component.upgrade.Upgrade;
import de.pnp.manager.component.upgrade.effect.EUpgradeEquipmentManipulator;
import de.pnp.manager.component.upgrade.effect.EquipmentUpgradeEffect;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Represents an {@link EquipableItem} that can be held and used.
 */
public class Equipment<E extends IEquipableItem> extends ItemStack<E> implements IEquipment {

    /**
     * The {@link Upgrade upgrades} of the {@link EquipableItem}.
     */
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
        return applyUpgradeEffects(EUpgradeEquipmentManipulator.SLOTS, getItem().getUpgradeSlots());
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
    protected int applyUpgradeEffects(EUpgradeEquipmentManipulator manipulator, int value) {
        return Math.round(applyUpgradeEffects(manipulator, (float) value));
    }

    /**
     * Applies the effects of the {@link #upgrades} to the value.
     */
    protected float applyUpgradeEffects(EUpgradeEquipmentManipulator manipulator, float value) {
        List<EquipmentUpgradeEffect> upgradeEffects = getUpgrades().stream()
            .flatMap(upgrade -> upgrade.getEffects().stream())
            .filter(EquipmentUpgradeEffect.class::isInstance).map(EquipmentUpgradeEffect.class::cast)
            .toList();
        List<EquipmentUpgradeEffect> additiveEffects = upgradeEffects.stream()
            .filter(effect -> effect.getCalculation() == ECalculation.ADDITIVE)
            .toList();
        List<EquipmentUpgradeEffect> multiplicativeEffects = upgradeEffects.stream()
            .filter(effect -> effect.getCalculation() == ECalculation.MULTIPLICATIVE)
            .toList();

        for (EquipmentUpgradeEffect effect : additiveEffects) {
            value = effect.apply(manipulator, value);
        }
        for (EquipmentUpgradeEffect effect : multiplicativeEffects) {
            value = effect.apply(manipulator, value);
        }

        return value;
    }
}
