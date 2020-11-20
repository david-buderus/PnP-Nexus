package model.item;

public class Armor extends Equipment {

    private int protection = 0;
    private double weight = 0;

    public int getProtection() {
        return protection;
    }

    public void setProtection(int protection) {
        this.protection = protection;
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