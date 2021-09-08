package model.upgrade;

import manager.Utility;
import model.ICurrency;

public class Upgrade implements IUpgrade {

    private String name;
    private String target;
    private int level;
    private int slots;
    private ICurrency cost;
    private String effect;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getSlots() {
        return slots;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public ICurrency getCost() {
        return cost;
    }

    public void setCost(ICurrency cost) {
        this.cost = cost;
    }

    public String getFullName() {
        return getName() + " " + Utility.asRomanNumber(getLevel());
    }
}
