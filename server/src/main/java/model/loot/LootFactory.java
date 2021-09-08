package model.loot;

import model.item.IItem;
import model.item.Item;

import java.util.Random;

public class LootFactory implements ILootFactory {

    private static final Random rand = new Random();

    private IItem item;
    private int maxAmount;
    private double chance;

    public LootFactory(IItem item, int maxAmount, double chance) {
        this.item = item;
        this.maxAmount = maxAmount;
        this.chance = chance;
    }

    public IItem getItem() {
        return item;
    }

    public void setItem(IItem item) {
        this.item = item;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }

    public double getChance() {
        return chance;
    }

    public void setChance(double chance) {
        this.chance = chance;
    }

    public Loot getLoot() {
        return getLoot(rand);
    }

    public Loot getLoot(Random random) {
        Loot loot = new Loot(item, 0);

        for (int i = 0; i < getMaxAmount(); i++) {
            if (random.nextDouble() < getChance()) {
                loot.addAmount(1);
            }
        }
        return loot;
    }
}
