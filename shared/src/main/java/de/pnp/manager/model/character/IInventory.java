package de.pnp.manager.model.character;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.pnp.manager.model.IItemList;
import de.pnp.manager.model.item.IItem;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface IInventory extends IItemList {

    double getMaxSize();

    @JsonIgnore
    default double getCurrentSize() {
        return stream().mapToDouble(IItem::getAmount).sum();
    }

    int getNumberOfSlots();

    @JsonIgnore
    default int getNumberOfOccupiedSlots() {
        return size();
    }

    @JsonIgnore
    default int getNumberOfFreeSlots() {
        return getNumberOfSlots() - getNumberOfOccupiedSlots();
    }

    @Override
    boolean add(IItem item);

    @Override
    boolean addAll(@NotNull Collection<? extends IItem> collection);

    /**
     * @param item which may be added to the inventory
     * @return if this inventory could hold additionally this item
     */
    default boolean hasSpaceFor(IItem item) {
        if (getCurrentSize() + item.getAmount() <= getMaxSize()) {
            return contains(item) || getNumberOfFreeSlots() > 0;
        }
        return false;
    }

    /**
     * @param collection which bay be added to the inventory
     * @return if this inventory could hold additionally these items
     */
    default boolean hasSpaceFor(@NotNull Collection<? extends IItem> collection) {

        if (collection.stream().mapToDouble(IItem::getAmount).sum() + getCurrentSize() <= getMaxSize()) {
            long notContainedItemStacks = collection.stream().filter(i -> !contains(i)).count();

            return notContainedItemStacks <= getNumberOfFreeSlots();
        }

        return false;
    }
}
