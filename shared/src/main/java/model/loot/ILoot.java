package model.loot;

import com.fasterxml.jackson.annotation.JsonIgnore;
import model.item.IItem;

public interface ILoot {

    IItem getItem();

    void setItem(IItem item);

    @JsonIgnore
    default String getName() {
        return getItem().getName();
    }

    int getAmount();

    float getAmountWithItemAmount();

    void setAmount(int amount);

    void addAmount(int amount);
}
