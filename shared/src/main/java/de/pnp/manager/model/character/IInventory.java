package de.pnp.manager.model.character;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.pnp.manager.model.IItemList;
import de.pnp.manager.model.item.IItem;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;

public interface IInventory extends Iterable<IItem> {

    int getMaxSize();

    int getMaxStackSize();

    @JsonIgnore
    default int getSize() {
        return getItems().size();
    }

    default void add(IItem item) {
        getItems().add(item);
    }

    default void addAll(Collection<IItem> items) {
        getItems().addAll(items);
    }

    IItemList getItems();

    @NotNull
    @Override
    default Iterator<IItem> iterator() {
        return getItems().iterator();
    }
}
