package de.pnp.manager.model;

import de.pnp.manager.model.item.IItem;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class ItemList extends ArrayList<IItem> implements IItemList {

    public ItemList() {
        super();
    }

    public ItemList(IItem... items) {
        this(Arrays.asList(items));
    }

    public ItemList(Collection<? extends IItem> collection) {
        super(collection);
    }

    @Override
    public boolean add(IItem item) {
        if (this.contains(item)) {
            this.get(this.indexOf(item)).addAmount(item.getAmount());
            return true;
        } else {
            IItem toAdd = item.copy();
            toAdd.setAmount(item.getAmount());
            return super.add(toAdd);
        }
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends IItem> collection) {
        for (IItem item : collection) {
            add(item);
        }
        return true;
    }

    @Override
    public boolean remove(Object object) {

        if (object instanceof IItem) {
            IItem item = (IItem) object;

            if (this.contains(item)) {
                IItem containedItem = this.get(this.indexOf(item));
                containedItem.addAmount(-item.getAmount());

                if (containedItem.getAmount() <= 0) {
                    super.remove(containedItem);
                }

                return true;
            }
        }

        return false;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> collection) {
        boolean removed = true;

        for (Object item : collection) {
            if (!remove(item)) {
                removed = false;
            }
        }
        return removed;
    }

    public Collection<? extends IItem> difference(Collection<? extends IItem> collection) {
        ItemList items = new ItemList();

        for (IItem item : collection) {
            float difference = difference(item);
            if (difference > 0) {
                IItem dif = item.copy();
                dif.setAmount(difference);
                items.add(dif);
            }
        }
        return items;
    }
}