package de.pnp.manager.component.inventory.equipment;

import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.interfaces.IDefensiveItem;
import de.pnp.manager.component.upgrade.effect.EUpgradeEquipmentManipulator;

/**
 * Represents an {@link IDefensiveItem} that can be held and used.
 */
public abstract class DefensiveEquipment<I extends IDefensiveItem> extends DamageableEquipment<I> {

    protected DefensiveEquipment(float stackSize, I item, int wear) {
        super(stackSize, item, wear);
    }

    /**
     * Returns the {@link IDefensiveItem#getArmor() defense} of the underlying {@link Item} with regard to the
     * {@link #getRelativeDurability() durability} and {@link #getUpgrades() upgrades}.
     */
    public int getArmor() {
        return Math.max(0, (int) Math.ceil(
                applyUpgradeEffects(EUpgradeEquipmentManipulator.ARMOR, getItem().getArmor()) * getRelativeDurability()));
    }

    /**
     * Returns the {@link IDefensiveItem#getArmor() defense} of the underlying {@link Item} with regard to the
     * {@link #getUpgrades() upgrades}.
     */
    public int getMaxArmor() {
        return Math.max(0, applyUpgradeEffects(EUpgradeEquipmentManipulator.ARMOR, getItem().getArmor()));
    }

    /**
     * Returns the {@link IDefensiveItem#getProtection() protection} of the underlying {@link Item} with regard to the
     * {@link #getUpgrades() upgrades}.
     */
    public int getMaxProtection() {
        return Math.max(0, applyUpgradeEffects(EUpgradeEquipmentManipulator.ARMOR, getItem().getProtection()));
    }

    /**
     * Returns the {@link IDefensiveItem#getProtection() protection} of the underlying {@link Item} with regard to the
     * {@link #getUpgrades() upgrades}.
     */
    public int getProtection() {
        return Math.max(0, applyUpgradeEffects(EUpgradeEquipmentManipulator.ARMOR, getItem().getProtection()));
    }

    /**
     * Returns the {@link IDefensiveItem#getWeight() weight} of the underlying {@link Item} with regard to the
     * {@link #getUpgrades() upgrades}.
     */
    public int getWeight() {
        return Math.round(
                applyUpgradeEffects(EUpgradeEquipmentManipulator.WEIGHT, getItem().getWeight()) * getRelativeDurability());
    }

    @Override
    public int getMaxDurability() {
        return getMaxArmor();
    }
}
