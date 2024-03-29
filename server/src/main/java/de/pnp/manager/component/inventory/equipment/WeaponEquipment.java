package de.pnp.manager.component.inventory.equipment;

import de.pnp.manager.component.inventory.equipment.interfaces.IHandheldEquipment;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.equipable.Weapon;
import de.pnp.manager.component.item.interfaces.IOffensiveItem;
import de.pnp.manager.component.upgrade.effect.EUpgradeManipulator;

/**
 * Represents an {@link IOffensiveItem} that can be held and used.
 */
public class WeaponEquipment extends DamageableEquipment<IOffensiveItem> implements
    IHandheldEquipment {

    public WeaponEquipment(float stackSize, IOffensiveItem item, int wear) {
        super(stackSize, item, wear);
    }

    /**
     * Returns the {@link Weapon#getDamage() damage} of the underlying {@link Item} with regard to the
     * {@link #getRelativeDurability() durability} and {@link #getUpgrades() upgrades}.
     */
    public int getDamage() {
        return Math.max(0, (int) Math.ceil(getMaxDamage() * getRelativeDurability()));
    }

    /**
     * Returns the {@link Weapon#getDamage() damage} of the underlying {@link Item} with regard to the
     * {@link #getUpgrades() upgrades}.
     */
    public int getMaxDamage() {
        return Math.max(0, applyUpgradeEffects(EUpgradeManipulator.DAMAGE, getItem().getDamage()));
    }

    @Override
    public int getHit() {
        return applyUpgradeEffects(EUpgradeManipulator.HIT, getItem().getHit());
    }

    @Override
    public float getInitiative() {
        return applyUpgradeEffects(EUpgradeManipulator.INITIATIVE, getItem().getInitiative());
    }

    @Override
    public int getMaxDurability() {
        return getMaxDamage();
    }
}
