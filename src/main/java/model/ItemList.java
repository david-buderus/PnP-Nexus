package model;

import model.item.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class ItemList extends ArrayList<Item> {

    public ItemList() {
        super();
    }

    public ItemList(Item... items) {
        this(Arrays.asList(items));
    }

    public ItemList(Collection<? extends Item> collection) {
        super(collection);
    }

    @Override
    public boolean add(Item item) {
        if (this.contains(item)) {
            this.get(this.indexOf(item)).addAmount(item.getAmount());
            return true;
        } else {
            Item toAdd = item.copy();
            toAdd.setAmount(item.getAmount());
            return super.add(toAdd);
        }
    }

    public boolean remove(Item item) {
        if (this.contains(item)) {
            this.get(this.indexOf(item)).addAmount(-item.getAmount());
            return true;
        }
        return false;
    }

    public boolean remove(Collection<? extends Item> collection) {
        for (Item item : collection) {
            if (!remove(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends Item> collection) {
        for (Item item : collection) {
            if (!add(item)) {
                return false;
            }
        }
        return true;
    }

    public boolean containsAmount(Item item) {
        return this.get(this.indexOf(item)).getAmount() >= item.getAmount();
    }

    public boolean containsAmount(Collection<? extends Item> items) {
        for (Item item : items) {
            if (!containsAmount(item)) {
                return false;
            }
        }

        return true;
    }

    public float difference(Item item) {
        if (this.contains(item)) {
            float existing = this.get(this.indexOf(item)).getAmount();
            if (existing < item.getAmount()) {
                return item.getAmount() - existing;
            }
            return 0;
        }
        return item.getAmount();
    }

    public Collection<? extends Item> difference(Collection<? extends Item> collection) {
        ItemList items = new ItemList();

        for (Item item : collection) {
            float difference = difference(item);
            if (difference > 0) {
                Item dif = item.copy();
                dif.setAmount(difference);
                items.add(dif);
            }
        }
        return items;
    }
}