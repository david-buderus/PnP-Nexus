package de.pnp.manager.model.loot;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.pnp.manager.model.ICurrency;
import de.pnp.manager.model.item.IItem;

import java.util.Collection;

public interface ILootTable {

    void add(String name, int amount, double chance);

    void add(ILootFactory factory);

    void add(ICurrency currency);

    void add(IItem item, int amount, double chance);

    void add(ILootTable other);

    Collection<ILootFactory> getLootFactories();

    void setLootFactories(Collection<ILootFactory> factories);

    @JsonIgnore
    Collection<ILoot> getLoot();
}
