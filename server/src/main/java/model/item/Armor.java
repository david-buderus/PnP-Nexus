package model.item;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Armor extends Equipment implements IArmor {

    protected final IntegerProperty protection;
    protected final IntegerBinding protectionWithWear;
    protected double weight;

    public Armor() {
        super();
        this.protection = new SimpleIntegerProperty(0);
        this.protectionWithWear = Bindings.createIntegerBinding(() -> Math.max(0, getProtection() - getWearStage()), protection, wearStage);
        this.weight = 0;
    }

    @Override
    protected boolean shouldBreak() {
        return getWearStage() > getProtection();
    }

    @Override
    public Armor copy() {
        Armor armor = (Armor) super.copy();
        armor.setProtection(this.getProtection());
        armor.setWeight(this.getWeight());

        return armor;
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

    public IntegerBinding protectionWithWearBinding() {
        return protectionWithWear;
    }

    public int getProtectionWithWear() {
        return protectionWithWear.get();
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
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