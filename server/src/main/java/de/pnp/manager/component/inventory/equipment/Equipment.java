package de.pnp.manager.component.inventory.equipment;

import com.google.common.base.Preconditions;
import de.pnp.manager.component.inventory.ItemStack;
import de.pnp.manager.component.inventory.equipment.interfaces.IEquipment;
import de.pnp.manager.component.item.equipable.EquipableItem;
import de.pnp.manager.component.item.interfaces.IEquipableItem;
import de.pnp.manager.component.upgrade.Upgrade;
import de.pnp.manager.component.upgrade.effect.AdditiveUpgradeEffect;
import de.pnp.manager.component.upgrade.effect.EUpgradeManipulator;
import de.pnp.manager.component.upgrade.effect.MultiplicativeUpgradeEffect;
import de.pnp.manager.component.upgrade.effect.UpgradeEffect;
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
    private Collection<Upgrade> upgrades;

    public Equipment(float amount, E item) {
        super(amount, item);
        upgrades = new ArrayList<>();
    }

    /**
     * Returns {@link EquipableItem#getUpgradeSlots()} in regard to the {@link #upgrades}.
     */
    public int getUpgradeSlots() {
        return applyUpgradeEffects(EUpgradeManipulator.SLOTS, getItem().getUpgradeSlots());
    }

    public int getRemainingUpgradeSlots() {
        return getUpgradeSlots() - getUpgrades().stream().mapToInt(Upgrade::getSlots).sum();
    }

    public Collection<Upgrade> getUpgrades() {
        return Collections.unmodifiableCollection(upgrades);
    }

    /**
     * Sets the {@link #upgrades} of the equipment.
     */
    public void setUpgrades(Collection<Upgrade> upgrades) {
        int requiredSlots = upgrades.stream().mapToInt(Upgrade::getSlots).sum();
        Preconditions.checkArgument(requiredSlots <= getUpgradeSlots(),
            "The needed %s slots of the upgrades exceeds the capacity of the item %s.",
            requiredSlots, getItem().getName());
        this.upgrades = upgrades;
    }

    /**
     * Adds an {@link Upgrade} to the equipment.
     */
    public void addUpgrade(Upgrade upgrade) {
        Preconditions.checkArgument(upgrade.getSlots() <= getRemainingUpgradeSlots(),
            "The needed %s slots of the upgrades exceeds the capacity of the item %s.",
            upgrade.getSlots() + getUpgradeSlots(), getItem().getName());
        upgrades.add(upgrade);
    }

    /**
     * Applies the effects of the {@link #upgrades} to the value.
     */
    protected int applyUpgradeEffects(EUpgradeManipulator manipulator, int value) {
        return Math.round(applyUpgradeEffects(manipulator, (float) value));
    }

    /**
     * Applies the effects of the {@link #upgrades} to the value.
     */
    protected float applyUpgradeEffects(EUpgradeManipulator manipulator, float value) {
        List<UpgradeEffect> upgradeEffects = getUpgrades().stream().flatMap(upgrade -> upgrade.getEffects().stream())
            .toList();
        List<UpgradeEffect> additiveEffects = upgradeEffects.stream().filter(AdditiveUpgradeEffect.class::isInstance)
            .toList();
        List<UpgradeEffect> multiplicativeEffects = upgradeEffects.stream()
            .filter(MultiplicativeUpgradeEffect.class::isInstance)
            .toList();
        List<UpgradeEffect> otherEffects = upgradeEffects.stream().filter(
                effect -> !(effect instanceof AdditiveUpgradeEffect) && !(effect instanceof MultiplicativeUpgradeEffect))
            .toList();

        for (UpgradeEffect effect : additiveEffects) {
            value = effect.apply(manipulator, value);
        }
        for (UpgradeEffect effect : multiplicativeEffects) {
            value = effect.apply(manipulator, value);
        }
        for (UpgradeEffect effect : otherEffects) {
            value = effect.apply(manipulator, value);
        }

        return value;
    }
}
