package de.pnp.manager.model;

import de.pnp.manager.model.item.IItem;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public interface IItemList extends List<IItem> {

    @Override
    boolean add(IItem item);

    boolean remove(IItem item);

    boolean remove(Collection<? extends IItem> collection);

    @Override
    boolean addAll(@NotNull Collection<? extends IItem> collection);

    default boolean containsAmount(IItem item) {
        return this.get(this.indexOf(item)).getAmount() >= item.getAmount();
    }

    default boolean containsAmount(Collection<? extends IItem> items) {
        for (IItem item : items) {
            if (!containsAmount(item)) {
                return false;
            }
        }

        return true;
    }

    default float difference(IItem item) {
        if (this.contains(item)) {
            float existing = this.get(this.indexOf(item)).getAmount();
            if (existing < item.getAmount()) {
                return item.getAmount() - existing;
            }
            return 0;
        }
        return item.getAmount();
    }

    Collection<? extends IItem> difference(Collection<? extends IItem> collection);
}
