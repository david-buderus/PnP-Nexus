package model.loot;

import model.ICurrency;
import model.item.IItem;

import java.util.Collection;

public interface ILootTable {

    void add(String name, int amount, double chance);

    void add(ILootFactory factory);

    void add(ICurrency currency);

    void add(IItem item, int amount, double chance);

    void add(ILootTable other);

    Collection<ILootFactory> getLootFactories();

    void setLootFactories(Collection<ILootFactory> factories);

    Collection<ILoot> getLoot();
}
