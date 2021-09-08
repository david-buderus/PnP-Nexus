package model.loot;

import model.item.IItem;

public interface ILoot {

    IItem getItem();

    void setItem(IItem item);

    default String getName() {
        return getItem().getName();
    }

    int getAmount();

    float getAmountWithItemAmount();

    void setAmount(int amount);

    void addAmount(int amount);
}
