package de.pnp.manager.component.inventory.equipment;

import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.interfaces.IDefensiveItem;
import de.pnp.manager.component.upgrade.effect.EItemEquipmentManipulator;

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
                applyItemEffects(EItemEquipmentManipulator.ARMOR, getItem().getArmor()) * getRelativeDurability()));
    }

    /**
     * Returns the {@link IDefensiveItem#getArmor() defense} of the underlying {@link Item} with regard to the
     * {@link #getUpgrades() upgrades}.
     */
    public int getMaxArmor() {
        return Math.max(0, applyItemEffects(EItemEquipmentManipulator.ARMOR, getItem().getArmor()));
    }

    /**
     * Returns the {@link IDefensiveItem#getProtection() protection} of the underlying {@link Item} with regard to the
     * {@link #getUpgrades() upgrades}.
     */
    public int getMaxProtection() {
        return Math.max(0, applyItemEffects(EItemEquipmentManipulator.ARMOR, getItem().getProtection()));
    }

    /**
     * Returns the {@link IDefensiveItem#getProtection() protection} of the underlying {@link Item} with regard to the
     * {@link #getUpgrades() upgrades}.
     */
    public int getProtection() {
        return Math.max(0, applyItemEffects(EItemEquipmentManipulator.ARMOR, getItem().getProtection()));
    }

    /**
     * Returns the {@link IDefensiveItem#getWeight() weight} of the underlying {@link Item} with regard to the
     * {@link #getUpgrades() upgrades}.
     */
    public int getWeight() {
        return Math.round(
                applyItemEffects(EItemEquipmentManipulator.WEIGHT, getItem().getWeight()) * getRelativeDurability());
    }

    @Override
    public int getMaxDurability() {
        return getMaxArmor();
    }
}
