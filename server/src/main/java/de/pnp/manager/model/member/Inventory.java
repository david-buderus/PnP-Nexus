package de.pnp.manager.model.member;

import de.pnp.manager.model.IItemList;
import de.pnp.manager.model.ItemList;

public class Inventory implements IInventory {

    @Override
    public int getMaxSize() {
        return 0;
    }

    @Override
    public int getMaxStackSize() {
        return 0;
    }

    @Override
    public IItemList getItems() {
        return new ItemList();
    }
}
