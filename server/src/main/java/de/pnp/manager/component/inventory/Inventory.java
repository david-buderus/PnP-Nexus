package de.pnp.manager.component.inventory;

import com.fasterxml.jackson.annotation.JsonCreator;
import de.pnp.manager.component.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * An inventory.
 */
public class Inventory {

    private final int maxSize;

    private final List<ItemStack<? extends Item>> items;

    @JsonCreator
    public Inventory(int maxSize, List<ItemStack<? extends Item>> items) {
        this.maxSize = maxSize;
        this.items = items.stream().map(ItemStack::clone).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Checks if the inventory has enough space left for the given stack.
     */
    public boolean hasSpaceFor(ItemStack<? extends Item> itemStack) {
        if (items.size() < maxSize) {
            return true;
        }
        float maxStackSize = itemStack.getItem().getMaximumStackSize();
        float remaining = itemStack.getStackSize();

        for (ItemStack<?> stack : items) {
            if (stack.canStack(itemStack)) {
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
        itemStack = itemStack.clone();
        float remaining = itemStack.getStackSize();

        for (ItemStack<?> stack : items) {
            if (stack.canStack(itemStack)) {
                remaining -= stack.addAmount(remaining);
            }
            if (Float.compare(remaining, 0) == 0) {
                break;
            }
        }
        if (remaining > 0) {
            itemStack.setAmount(remaining);
            items.add(itemStack);
        }
        return true;
    }

    /**
     * Removes an item from the inventory.
     */
    public void removeItem(ItemStack<? extends Item> itemStack) {
        float remaining = itemStack.getStackSize();

        List<ItemStack<? extends Item>> itemsToRemove = new ArrayList<>();

        for (ItemStack<? extends Item> stack : items) {
            if (stack.canStack(itemStack)) {
                if (remaining >= stack.getStackSize()) {
                    itemsToRemove.add(stack);
                    remaining -= stack.getStackSize();
                } else {
                    remaining -= stack.subtractAmount(remaining);
                }
                if (Float.compare(remaining, 0) == 0) {
                    break;
                }
            }
        }
        for (ItemStack<? extends Item> toRemove : itemsToRemove) {
            items.remove(toRemove);
        }
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Inventory inventory = (Inventory) o;
        return maxSize == inventory.maxSize && Objects.equals(items, inventory.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maxSize, items);
    }
}
