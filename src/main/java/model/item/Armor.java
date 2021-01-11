package model.item;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Armor extends Equipment {

    private final IntegerProperty protection;
    private double weight;

    public Armor() {
        super();
        this.protection = new SimpleIntegerProperty(0);
        this.weight = 0;
    }

    public int getProtection() {
        return protection.get();
    }

    public IntegerProperty protectionProperty() {
        return protection;
    }

    public void setProtection(int protection) {
        this.protection.set(protection);
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public Armor copy() {
        Armor armor = (Armor) super.copy();
        armor.setProtection(this.getProtection());
        armor.setWeight(this.getWeight());

        return armor;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof Armor) || !super.equals(o)) {
            return false;
        }

        Armor other = (Armor) o;

        return this.getProtection() == other.getProtection() && this.getWeight() == other.getWeight();
    }
}