package de.pnp.manager.model;

import de.pnp.manager.model.item.IItem;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface IItemList extends Collection<IItem> {

    @Override
    boolean add(IItem item);

    @Override
    boolean addAll(@NotNull Collection<? extends IItem> collection);

    @Override
    boolean remove(Object object);

    @Override
    boolean removeAll(@NotNull Collection<?> collection);

    default boolean containsAmount(IItem item) {
        return this.contains(item) && this.get(this.indexOf(item)).getAmount() >= item.getAmount();
    }

    IItem get(int position);

    int indexOf(Object item);

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
