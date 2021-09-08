package model.loot;

import com.fasterxml.jackson.annotation.JsonIgnore;
import model.item.IItem;

import java.util.Random;

public interface ILootFactory {

    @JsonIgnore
    default String getName() {
        return getItem().getName();
    }

    IItem getItem();

    void setItem(IItem item);

    int getMaxAmount();

    void setMaxAmount(int maxAmount);

    double getChance();

    void setChance(double chance);

    @JsonIgnore
    ILoot getLoot();

    @JsonIgnore
    ILoot getLoot(Random random);
}
