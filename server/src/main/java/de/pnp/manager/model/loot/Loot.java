package de.pnp.manager.model.loot;

import de.pnp.manager.main.LanguageUtility;
import de.pnp.manager.model.item.Equipment;
import de.pnp.manager.model.item.IItem;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Loot implements ILoot {

    private IItem item;
    private final IntegerProperty amount;

    public Loot(IItem item) {
        this(item, 1);
    }

    public Loot(IItem item, int amount) {
        this.item = item;
        this.amount = new SimpleIntegerProperty(amount);
    }

    @Override
    public IItem getItem() {
        return item;
    }

    @Override
    public void setItem(IItem item) {
        this.item = item;
    }

    @Override
    public String getName() {
        if (item instanceof Equipment && !((Equipment) item).getUpgrades().isEmpty()) {
            return item.getName() + " (" + LanguageUtility.getMessage("item.upgraded") + ")";
        }
        return item.getName();
    }

    @Override
    public int getAmount() {
        return amount.get();
    }

    @Override
    public float getAmountWithItemAmount() {
        return amount.get() * item.getAmount();
    }

    @Override
    public void setAmount(int amount) {
        this.amount.set(amount);
    }

    @Override
    public void addAmount(int amount) {
        setAmount(getAmount() + amount);
    }

    public IntegerProperty amountProperty() {
        return this.amount;
    }
}
