package de.pnp.manager.component.inventory.equipment;

import de.pnp.manager.component.inventory.equipment.interfaces.IHandheldEquipment;
import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.item.equipable.Weapon;
import de.pnp.manager.component.item.interfaces.IOffensiveItem;

/**
 * Represents an {@link IOffensiveItem} that can be held and used.
 */
public class WeaponEquipment extends DamageableEquipment<IOffensiveItem> implements
    IHandheldEquipment {

    public WeaponEquipment(IOffensiveItem item, int durability) {
        super(item, durability);
    }

    /**
     * Returns the {@link Weapon#getDamage() damage} of the underlying {@link Item} with regard to the
     * {@link #getRelativeDurability() durability}.
     */
    public int getDamage() {
        return Math.round(getItem().getDamage() * getRelativeDurability());
    }

    @Override
    public int getHit() {
        return getItem().getHit();
    }

    @Override
    public float getInitiative() {
        return getItem().getInitiative();
    }
}
