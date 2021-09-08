package model.loot;

import manager.Database;
import manager.LanguageUtility;
import manager.Utility;
import model.ICurrency;
import model.item.IItem;
import model.item.Item;
import org.apache.commons.configuration2.Configuration;

import java.util.ArrayList;
import java.util.Collection;

public class LootTable implements ILootTable {

    private ArrayList<ILootFactory> list;

    public LootTable() {
        this.list = new ArrayList<>();
    }

    public void add(String name, int amount, double chance) {
        list.add(new LootFactory(Database.getItem(name), amount, chance));
    }

    public void add(ILootFactory factory) {
        list.add(factory);
    }

    public void add(ICurrency currency) {
        Configuration config = Utility.getConfig();
        int silverToCopper = config.getInt("coin.silver.toCopper");
        int goldToCopper = config.getInt("coin.gold.toSilver") * silverToCopper;

        int value = currency.getCoinValue();
        int gp = value / goldToCopper;
        value -= gp * goldToCopper;

        int sp = value / silverToCopper;
        value -= sp * silverToCopper;

        int cp = value;

        Item gold = Database.getItem(LanguageUtility.getMessage("coin.gold"));
        Item silver = Database.getItem(LanguageUtility.getMessage("coin.silver"));
        Item copper = Database.getItem(LanguageUtility.getMessage("coin.copper"));

        add(gold, gp, 1);
        add(silver, sp, 1);
        add(copper, cp, 1);
    }

    public void add(IItem item, int amount, double chance) {
        list.add(new LootFactory(item, amount, chance));
    }

    public void add(ILootTable other) {
        this.list.addAll(other.getLootFactories());
    }

    @Override
    public Collection<ILootFactory> getLootFactories() {
        return list;
    }

    @Override
    public void setLootFactories(Collection<ILootFactory> factories) {
        this.list = new ArrayList<>(factories);
    }

    @Override
    public Collection<ILoot> getLoot() {

        ArrayList<ILoot> lootList = new ArrayList<>();

        for (ILootFactory factory : list) {
            ILoot loot = factory.getLoot();
            ILoot own = getLoot(factory.getItem(), lootList);

            if (own != null) {
                own.addAmount(loot.getAmount());
            } else {
                if (loot.getAmount() > 0) {
                    lootList.add(loot);
                }
            }
        }

        return lootList;
    }

    private ILoot getLoot(IItem artifact, ArrayList<ILoot> list) {
        for (ILoot l : list) {
            if (l.getItem().equals(artifact)) {
                return l;
            }
        }
        return null;
    }
}
