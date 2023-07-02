package de.pnp.manager.component.inventory.equipment;

import de.pnp.manager.component.inventory.equipment.interfaces.IHandheldEquipment;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.interfaces.IDefensiveItem;
import de.pnp.manager.component.item.interfaces.IHandheldItem;
import de.pnp.manager.component.upgrade.effect.EUpgradeManipulator;

/**
 * Represents an {@link IDefensiveItem} that can be held and used.
 */
public class DefensiveEquipment extends DamageableEquipment<IDefensiveItem> implements
    IHandheldEquipment {

    public DefensiveEquipment(float stackSize, IDefensiveItem item, int durability) {
        super(stackSize, item, durability);
    }

    /**
     * Returns the {@link IDefensiveItem#getArmor() defense} of the underlying {@link Item} with regard to the
     * {@link #getRelativeDurability() durability} and {@link #getUpgrades() upgrades}.
     */
    public int getArmor() {
        return Math.round(
            applyUpgradeEffects(EUpgradeManipulator.ARMOR, getItem().getArmor()) * getRelativeDurability());
    }

    /**
     * Returns the {@link IDefensiveItem#getWeight() weight} of the underlying {@link Item} with regard to the
     * {@link #getUpgrades() upgrades}.
     */
    public int getWeight() {
        return Math.round(
            applyUpgradeEffects(EUpgradeManipulator.WEIGHT, getItem().getWeight()) * getRelativeDurability());
    }

    @Override
    public int getHit() {
        if (getItem() instanceof IHandheldItem handHoldItem) {
            return applyUpgradeEffects(EUpgradeManipulator.HIT, handHoldItem.getHit());
        }
        throw new AssertionError("Only HandholdItems have hit.");
    }

    @Override
    public float getInitiative() {
        if (getItem() instanceof IHandheldItem handHoldItem) {
            return handHoldItem.getInitiative();
        }
        throw new AssertionError("Only HandholdItems have initiative.");
    }
}
