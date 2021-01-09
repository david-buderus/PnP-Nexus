package model.loot;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import manager.LanguageUtility;
import model.item.Equipment;
import model.item.Item;

public class Loot {

    private Item item;
    private final IntegerProperty amount;

    public Loot(Item item) {
        this(item, 1);
    }

    public Loot(Item item, int amount) {
        this.item = item;
        this.amount = new SimpleIntegerProperty(amount);
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getName() {
        if (item instanceof Equipment && !((Equipment) item).getUpgrades().isEmpty()) {
            return item.getName() + " (" + LanguageUtility.getMessage("item.upgraded") + ")";
        }
        return item.getName();
    }

    public int getAmount() {
        return amount.get();
    }

    public void setAmount(int amount) {
        this.amount.set(amount);
    }

    public void addAmount(int amount) {
        setAmount(getAmount() + amount);
    }

    public IntegerProperty amountProperty() {
        return this.amount;
    }
}
