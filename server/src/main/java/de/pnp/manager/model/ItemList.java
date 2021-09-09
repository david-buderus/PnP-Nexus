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
    public boolean remove(IItem item) {
        if (this.contains(item)) {
            this.get(this.indexOf(item)).addAmount(-item.getAmount());
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(Collection<? extends IItem> collection) {
        for (IItem item : collection) {
            if (!remove(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends IItem> collection) {
        for (IItem item : collection) {
            if (!add(item)) {
                return false;
            }
        }
        return true;
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