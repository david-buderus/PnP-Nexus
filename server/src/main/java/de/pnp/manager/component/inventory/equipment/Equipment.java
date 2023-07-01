package de.pnp.manager.component.inventory.equipment;

import de.pnp.manager.component.inventory.ItemStack;
import de.pnp.manager.component.inventory.equipment.interfaces.IEquipment;
import de.pnp.manager.component.item.interfaces.IEquipableItem;

public class Equipment<E extends IEquipableItem> extends ItemStack<E> implements IEquipment {
    // Todo Verzauberungen etc

    public Equipment(E item) {
        super(1, item);
    }

    @Override
    public void addAmount(float amount) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void subtractAmount(float amount) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAmount(float amount) {
        throw new UnsupportedOperationException();
    }
}
