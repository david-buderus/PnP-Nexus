package model.upgrade;

import com.fasterxml.jackson.annotation.JsonIgnore;
import manager.Utility;

public class Upgrade {

    private String name;
    private String target;
    private int level;
    private int slots;
    private int cost;
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

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    @JsonIgnore
    public String getFullName() {
        return getName() + " " + Utility.asRomanNumber(getLevel());
    }
}
