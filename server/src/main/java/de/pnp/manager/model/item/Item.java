package de.pnp.manager.model.item;

import de.pnp.manager.main.Database;
import de.pnp.manager.model.Currency;
import de.pnp.manager.model.ICurrency;
import de.pnp.manager.model.IRarity;
import de.pnp.manager.model.Rarity;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;

import java.lang.reflect.InvocationTargetException;

/**
 * Represents an item in the database
 * If you need an item with a specific name use
 * {@link Database#getItem(String)}.
 */
public class Item implements IItem {

    public static final Item EMPTY_ITEM = new Item();

    protected String name;
    protected String type;
    protected String subtype;
    protected String requirement;
    protected String effect;
    protected IRarity rarity;
    protected ICurrency currency;
    protected int tier;
    protected FloatProperty amount;

    /**
     * Use this only if you know what you do.
     * Use {@link Database#getItem(String)} instead.
     */
    public Item() {
        this.name = "";
        this.type = "";
        this.subtype = "";
        this.requirement = "";
        this.effect = "";
        this.rarity = Rarity.COMMON;
        this.currency = new Currency(0);
        this.tier = 1;
        this.amount = new SimpleFloatProperty(1);
    }

    /**
     * Use this only if you know what you do.
     * Use {@link Database#getItem(String)} instead.
     */
    public Item(String name) {
        this();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public ICurrency getCurrency() {
        return currency;
    }

    public void setCurrency(ICurrency currency) {
        this.currency = currency;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getSubtype() {
        return subtype;
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

    public IRarity getRarity() {
        return rarity;
    }

    public void setRarity(IRarity rarity) {
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

    public void setAmount(float amount) {
        this.amount.set(amount);
    }

    public void addAmount(float amount) {
        this.amount.set(getAmount() + amount);
    }

    public FloatProperty amountProperty() {
        return this.amount;
    }

    public ICurrency getCurrencyWithAmount() {
        return currency.multiply(getAmount());
    }

    public boolean isTradeable() {
        return currency.isTradeable();
    }

    public Item copy() {
        try {
            Item item = this.getClass().getConstructor().newInstance();
            item.setName(this.getName());
            item.setType(this.getType());
            item.setSubtype(this.getSubtype());
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

        return this.getName().equals(other.getName()) && this.getType().equals(other.getType())
                && this.getRequirement().equals(other.getRequirement()) && this.getEffect().equals(other.getEffect())
                && this.getRarity().equals(other.getRarity()) && this.getSubtype().equals(other.getSubtype())
                && this.getCurrency().equals(other.getCurrency());
    }

    @Override
    public String toString() {
        return name;
    }
}
