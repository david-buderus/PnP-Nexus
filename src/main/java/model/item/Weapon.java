package model.item;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Weapon extends Equipment {

    private String initiative;
    private String dice;
    private final IntegerProperty damage;
    protected final IntegerBinding damageWithWear;
    private int hit;

    public Weapon() {
        super();
        this.initiative = "0";
        this.dice = "";
        this.damage = new SimpleIntegerProperty(0);
        this.damageWithWear = Bindings.createIntegerBinding(() -> Math.max(0, getDamage() - getWear()), damage, wear);
        this.hit = 0;
    }

    @Override
    protected boolean shouldBreak() {
        return getWear() > getDamage();
    }

    @Override
    public Weapon copy() {
        Weapon weapon = (Weapon) super.copy();
        weapon.setInitiative(this.getInitiative());
        weapon.setDice(this.getDice());
        weapon.setDamage(this.getDamage());
        weapon.setHit(this.getHit());

        return weapon;
    }

    public String getInitiative() {
        return initiative;
    }

    public void setInitiative(String initiative) {
        this.initiative = initiative;
    }

    public String getDice() {
        return dice;
    }

    public void setDice(String dice) {
        this.dice = dice;
    }

    public int getDamage() {
        return damage.get();
    }

    public IntegerProperty damageProperty() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage.set(damage);
    }

    public IntegerBinding damageWithWearBinding() {
        return damageWithWear;
    }

    public int getHit() {
        return hit;
    }

    public void setHit(int hit) {
        this.hit = hit;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof Weapon) || !super.equals(o)) {
            return false;
        }

        Weapon other = (Weapon) o;

        return this.getInitiative().equals(other.getInitiative()) && this.getDice().equals(other.getDice())
                && this.getDamage() == other.getDamage() && this.getHit() == other.getHit();
    }
}
