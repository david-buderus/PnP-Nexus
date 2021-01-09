package model.item;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import manager.Database;
import model.Currency;
import model.Rarity;

import java.lang.reflect.InvocationTargetException;

/**
 * Represents an item in the database
 * If you need an item with a specific name use
 * {@link Database#getItem(String)}.
 */
public class Item {

    protected String name = "";
    protected String typ = "";
    protected String subTyp = "";
    protected String requirement = "";
    protected String effect = "";
    protected Rarity rarity = Rarity.common;
    protected Currency currency = new Currency(0);
    protected int tier = 1;
    protected FloatProperty amount = new SimpleFloatProperty(1);

    /**
     * Use this only if you know what you do.
     * Use {@link Database#getItem(String)} instead.
     */
    public Item() { }

    /**
     * Use this only if you know what you do.
     * Use {@link Database#getItem(String)} instead.
     */
    public Item(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public String getTyp() {
        return typ;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public void setSubTyp(String subTyp) {
        this.subTyp = subTyp;
    }

    public String getSubTyp() {
        return subTyp;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement.trim();
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    public float getAmount() {
        return amount.get();
    }

    public String getPrettyAmount() {
        float amount = getAmount();
        if (amount == (int) amount) {
            return Integer.toString((int) amount);
        } else {
            return Float.toString(amount);
        }
    }

    public void setAmount(float amount) {
        this.amount.set(amount);
    }

    public void addAmount(float amount) {
        this.amount.set(getAmount() + amount);
    }

    public FloatProperty amountProperty() {
        return this.amount;
    }

    public Currency getCurrencyWithAmount() {
        return currency.multiply(getAmount());
    }

    public boolean isTradeable() {
        return currency.isTradeable();
    }

    public Item copy() {
        try {
            Item item = this.getClass().getConstructor().newInstance();
            item.setName(this.getName());
            item.setTyp(this.getTyp());
            item.setSubTyp(this.getSubTyp());
            item.setRarity(this.getRarity());
            item.setRequirement(this.getRequirement());
            item.setEffect(this.getEffect());
            item.setCurrency(this.getCurrency());

            return item;

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof Item)) {
            return false;
        }

        Item other = (Item) o;

        return this.getName().equals(other.getName()) && this.getTyp().equals(other.getTyp())
                && this.getRequirement().equals(other.getRequirement()) && this.getEffect().equals(other.getEffect())
                && this.getRarity().equals(other.getRarity()) && this.getSubTyp().equals(other.getSubTyp())
                && this.getCurrency().equals(other.getCurrency());
    }

    @Override
    public String toString() {
        return name;
    }
}
