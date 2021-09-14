package de.pnp.manager.model.loot;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.pnp.manager.model.item.IItem;

public interface ILoot {

    IItem getItem();

    void setItem(IItem item);

    @JsonIgnore
    default String getName() {
        return getItem().getName();
    }

    int getAmount();

    @JsonIgnore
    float getAmountWithItemAmount();

    void setAmount(int amount);

    void addAmount(int amount);
}
