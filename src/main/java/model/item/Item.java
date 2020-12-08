package model.item;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import manager.Database;

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
    protected String rarity = "";
    protected String cost = "";
    protected int tier = 1;
    protected FloatProperty amount = new SimpleFloatProperty(1);

    /**
     * Use this only if you know what you do.
     * Use {@link Database#getItem(String)} instead.
     */
    public Item() {
    }

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

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getCost() {
        return cost;
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
        this.requirement = requirement;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
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

    public int getCostAsCopper() {
        return Math.round(getCostOfOneAsCopper() * getAmount());
    }

    public int getCostOfOneAsCopper() {
        int value = 0;
        StringBuilder number = new StringBuilder();

        for (int i = 0; i < cost.length(); i++) {
            char c = cost.charAt(i);
            if (Character.isDigit(c)) {
                number.append(c);
            } else {
                switch (c) {
                    case 'K':
                        value += Integer.parseInt(number.toString());
                        number = new StringBuilder();
                        break;
                    case 'S':
                        value += Integer.parseInt(number.toString()) * 100;
                        number = new StringBuilder();
                        break;
                    case 'G':
                        value += Integer.parseInt(number.toString()) * 10000;
                        number = new StringBuilder();
                        break;
                }
            }
        }

        return value;
    }

    public boolean isTradeable() {
        return !cost.equals("Nicht Handelbar");
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
            item.setCost(this.getCost());

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
                && this.getCost().equals(other.getCost());
    }

    @Override
    public String toString() {
        return name;
    }
}
