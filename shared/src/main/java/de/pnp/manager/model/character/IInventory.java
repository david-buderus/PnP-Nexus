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
}
