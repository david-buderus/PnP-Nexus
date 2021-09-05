package model.loot;

import manager.Database;
import manager.LanguageUtility;
import manager.Utility;
import model.Currency;
import model.item.Item;
import org.apache.commons.configuration2.Configuration;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Collection;

public class LootTable {

    private final ArrayList<LootFactory> list;

    public LootTable() {
        this.list = new ArrayList<>();
    }

    public void add(String name, int amount, double chance) {
        list.add(new LootFactory(Database.getItem(name), amount, chance));
    }

    public void add(LootFactory factory) {
        list.add(factory);
    }

    public void add(Currency currency) {
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

    public void add(Item item, int amount, double chance) {
        list.add(new LootFactory(item, amount, chance));
    }

    public void add(LootTable other) {
        this.list.addAll(other.list);
    }

    public Collection<Loot> getLoot() {

        ArrayList<Loot> lootList = new ArrayList<>();

        for (LootFactory factory : list) {
            Loot loot = factory.getLoot();
            Loot own = getLoot(factory.getItem(), lootList);

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

    private Loot getLoot(Item artifact, ArrayList<Loot> list) {
        for (Loot l : list) {
            if (l.getItem().equals(artifact)) {
                return l;
            }
        }
        return null;
    }
}
