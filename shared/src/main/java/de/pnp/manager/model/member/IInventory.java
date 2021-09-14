package de.pnp.manager.model.member;

import de.pnp.manager.model.IItemList;

public interface IInventory {

    int getMaxSize();

    int getMaxStackSize();

    default int getSize() {
        return getItems().size();
    }

    IItemList getItems();
}
