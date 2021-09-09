package de.pnp.manager.model.member.generation;

import de.pnp.manager.main.Database;
import de.pnp.manager.model.item.Item;
import de.pnp.manager.model.loot.LootTable;
import de.pnp.manager.model.member.BattleMember;

public class Drop {

    protected Item item;
    protected float chance;
    protected int baseAmount;
    protected int multiplicativeAmount;
    protected float levelMultiplication;
    protected float tierMultiplication;
    protected int minLevel;
    protected int minTier;
    protected int maxLevel;
    protected int maxTier;

    public Drop() {
    }

    public void addToLootTable(LootTable lootTable, BattleMember member) {
        if (member.getLevel() >= minLevel && member.getLevel() <= maxLevel &&
                member.getTier() >= minTier && member.getTier() <= maxTier) {
            float amount = this.multiplicativeAmount;

            if (levelMultiplication != 0) {
                amount *= levelMultiplication * member.getLevel();
            }
            if (tierMultiplication != 0) {
                amount *= tierMultiplication * member.getTier();
            }

            lootTable.add(item, Math.round(baseAmount + amount), chance);
        }
    }

    public void setName(String name) {
        setItem(Database.getItem(name));
    }

    public String getName() {
        return getItem().getName();
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public float getChance() {
        return chance;
    }

    public void setChance(float chance) {
        this.chance = chance;
    }

    public int getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(int baseAmount) {
        this.baseAmount = baseAmount;
    }

    public int getMultiplicativeAmount() {
        return multiplicativeAmount;
    }

    public void setMultiplicativeAmount(int multiplicativeAmount) {
        this.multiplicativeAmount = multiplicativeAmount;
    }

    public float getLevelMultiplication() {
        return levelMultiplication;
    }

    public void setLevelMultiplication(float levelMultiplication) {
        this.levelMultiplication = levelMultiplication;
    }

    public float getTierMultiplication() {
        return tierMultiplication;
    }

    public void setTierMultiplication(float tierMultiplication) {
        this.tierMultiplication = tierMultiplication;
    }

    public int getMinLevel() {
        return minLevel;
    }

    public void setMinLevel(int minLevel) {
        this.minLevel = minLevel;
    }

    public int getMinTier() {
        return minTier;
    }

    public void setMinTier(int minTier) {
        this.minTier = minTier;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public int getMaxTier() {
        return maxTier;
    }

    public void setMaxTier(int maxTier) {
        this.maxTier = maxTier;
    }
}
