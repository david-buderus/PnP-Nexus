package de.pnp.manager.component.inventory;

import de.pnp.manager.component.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * An inventory.
 */
public class Inventory {

    private final int maxSize;

    private final List<ItemStack<? extends Item>> items;

    public Inventory(int maxSize, List<ItemStack<? extends Item>> items) {
        this.maxSize = maxSize;
        this.items = items;
    }

    /**
     * Checks if the inventory has enough space left for the given stack.
     */
    public boolean hasSpaceFor(ItemStack<?> itemStack) {
        if (items.size() < maxSize - 1) {
            return true;
        }
        float maxStackSize = itemStack.getItem().getMaximumStackSize();
        float remaining = itemStack.getStackSize();

        for (ItemStack<?> stack : items) {
            if (Objects.equals(stack.getItem(), itemStack.getItem())) {
                remaining -= maxStackSize - stack.getStackSize();
            }
        }

        return remaining <= 0;
    }

    /**
     * Adds an item to inventory.
     */
    public boolean addItem(ItemStack<? extends Item> itemStack) {
        if (!hasSpaceFor(itemStack)) {
            return false;
        }
        float remaining = itemStack.getStackSize();

        for (ItemStack<?> stack : items) {
            if (Objects.equals(stack.getItem(), itemStack.getItem())) {
                remaining -= stack.addAmount(remaining);
            }
        }
        if (remaining > 0) {
            items.add(itemStack);
        }
        return true;
    }

    public int getMaxSize() {
        return maxSize;
    }

    /**
     * Returns a copy of the underlying item list.
     */
    public List<ItemStack<? extends Item>> getItems() {
        return new ArrayList<>(items);
    }
}
