package model.loot;

import model.item.IItem;

import java.util.Random;

public interface ILootFactory {

    default String getName() {
        return getItem().getName();
    }

    IItem getItem();

    void setItem(IItem item);

    int getMaxAmount();

    void setMaxAmount(int maxAmount);

    double getChance();

    void setChance(double chance);

    ILoot getLoot();

    ILoot getLoot(Random random);
}
