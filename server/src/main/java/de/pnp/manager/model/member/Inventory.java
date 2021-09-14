package de.pnp.manager.model.member;

import de.pnp.manager.model.IItemList;
import de.pnp.manager.model.ItemList;
import de.pnp.manager.model.item.IItem;

import java.util.Collection;
import java.util.Collections;

public class Inventory implements IInventory {

    public static final Inventory EMPTY_INVENTORY = new Inventory(0, 0, Collections.emptyList());

    protected int maxSize;
    protected int maxStackSize;
    protected IItemList items;

    public Inventory(int maxSize, int maxStackSize, Collection<IItem> items) {
        this.maxSize = maxSize;
        this.maxStackSize = maxStackSize;
        this.items = new ItemList(items);
    }

    @Override
    public int getMaxSize() {
        return maxSize;
    }

    @Override
    public int getMaxStackSize() {
        return maxStackSize;
    }

    @Override
    public IItemList getItems() {
        return items;
    }
}
