package de.pnp.manager.model.character;

import de.pnp.manager.model.ItemList;
import de.pnp.manager.model.item.IItem;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class Inventory extends ItemList implements IInventory {

    public static final Inventory EMPTY_INVENTORY = new Inventory(0, 0);
    public static final Inventory UNLIMITED_INVENTORY = new Inventory(Double.MAX_VALUE, Integer.MAX_VALUE);

    protected double maxSize;
    protected int numberOfSlots;

    public Inventory(double maxSize, int numberOfSlots) {
        super();
        this.maxSize = maxSize;
        this.numberOfSlots = numberOfSlots;
    }

    public Inventory(double maxSize, int numberOfSlots, IItem... items) {
        super(items);
        this.maxSize = maxSize;
        this.numberOfSlots = numberOfSlots;
    }

    public Inventory(double maxSize, int numberOfSlots, Collection<IItem> items) {
        super(items);
        this.maxSize = maxSize;
        this.numberOfSlots = numberOfSlots;
    }

    @Override
    public boolean add(IItem item) {
        if (getCurrentSize() + item.getAmount() <= getMaxSize()) {
            if (contains(item)) {
                return super.add(item);
            } else {
                if (getNumberOfOccupiedSlots() < getNumberOfSlots()) {
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

            if (getNumberOfOccupiedSlots() + notContainedItemStacks <= getNumberOfSlots()) {
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
    public int getNumberOfSlots() {
        return numberOfSlots;
    }

    public void setMaxSize(double maxSize) {
        this.maxSize = maxSize;
    }

    public void setNumberOfSlots(int numberOfSlots) {
        this.numberOfSlots = numberOfSlots;
    }
}
