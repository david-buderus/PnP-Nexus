package model.loot;

import model.item.Item;

import java.util.Random;

public class LootFactory {

    private static final Random rand = new Random();

    private Item item;
    private int maxAmount;
    private double chance;

    public LootFactory(Item item, int maxAmount, double chance) {
        this.item = item;
        this.maxAmount = maxAmount;
        this.chance = chance;
    }

    public String getName(){
        return item.getName();
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
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

    public Loot getLoot(){
        Loot loot = new Loot(item, 0);

        for(int i=0; i<getMaxAmount(); i++) {
            if (rand.nextDouble() < getChance()) {
                loot.addAmount(1);
            }
        }
        return loot;
    }
}
