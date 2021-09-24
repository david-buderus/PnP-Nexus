package de.pnp.manager.model.character;

import de.pnp.manager.model.ItemList;
import de.pnp.manager.model.item.IItem;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class Inventory extends ItemList implements IInventory {

    public static final Inventory EMPTY_INVENTORY = new Inventory(0, 0);

    protected double maxSize;
    protected int maxStackSize;

    public Inventory(double maxSize, int maxStackSize) {
        super();
        this.maxSize = maxSize;
        this.maxStackSize = maxStackSize;
    }

    public Inventory(double maxSize, int maxStackSize, IItem... items) {
        super(items);
        this.maxSize = maxSize;
        this.maxStackSize = maxStackSize;
    }

    public Inventory(double maxSize, int maxStackSize, Collection<IItem> items) {
        super(items);
        this.maxSize = maxSize;
        this.maxStackSize = maxStackSize;
    }

    @Override
    public boolean add(IItem item) {
        if (getCurrentSize() + item.getAmount() <= getMaxSize()) {
            if (contains(item)) {
                return super.add(item);
            } else {
                if (getCurrentStackSize() < getMaxStackSize()) {
                    return super.add(item);
                }
            }

        }
        return false;
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends IItem> collection) {
        if (collection.stream().mapToDouble(IItem::getAmount).sum() + getCurrentSize() <= getMaxSize()) {
            long notContainedItemStacks = collection.stream().filter(i -> !contains(i)).count();

            if (getCurrentStackSize() + notContainedItemStacks <= getMaxStackSize()) {
                return super.addAll(collection);
            }
        }

        return false;
    }

    @Override
    public double getMaxSize() {
        return maxSize;
    }

    @Override
    public int getMaxStackSize() {
        return maxStackSize;
    }

    public void setMaxSize(double maxSize) {
        this.maxSize = maxSize;
    }

    public void setMaxStackSize(int maxStackSize) {
        this.maxStackSize = maxStackSize;
    }
}
