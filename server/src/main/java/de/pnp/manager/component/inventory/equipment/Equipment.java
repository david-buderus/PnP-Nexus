package de.pnp.manager.component.inventory.equipment;

import de.pnp.manager.component.inventory.ItemStack;
import de.pnp.manager.component.inventory.equipment.interfaces.IEquipment;
import de.pnp.manager.component.item.equipable.EquipableItem;
import de.pnp.manager.component.item.interfaces.IEquipableItem;
import de.pnp.manager.component.upgrade.Upgrade;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents an {@link EquipableItem} that can be held and used.
 */
public class Equipment<E extends IEquipableItem> extends ItemStack<E> implements IEquipment {

    /**
     * The {@link Upgrade upgrades} of the {@link EquipableItem}.
     */
    private Collection<Upgrade> upgrades;

    public Equipment(E item) {
        super(1, item);
        upgrades = new ArrayList<>();
    }

    public Collection<Upgrade> getUpgrades() {
        return upgrades;
    }

    public void setUpgrades(Collection<Upgrade> upgrades) {
        this.upgrades = upgrades;
    }
}
