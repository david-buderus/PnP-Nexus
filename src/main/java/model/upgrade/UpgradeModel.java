package model.upgrade;

import manager.Utility;

import java.util.ArrayList;

public class UpgradeModel {
    private String name;
    private String target;
    private int slots;
    private String cost;
    private String mana;
    private String effect;
    private final ArrayList<String> materialList = new ArrayList<>();
    private int level;
    private String requirement;

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

    public int getSlots() {
        return slots;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getMana() {
        return mana;
    }

    public void setMana(String mana) {
        this.mana = mana;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public void setMaterials(String empty) { }

    public String getMaterials(){
        return String.join("\n", materialList);
    }

    public void addMaterial(String material){
        this.materialList.add(material);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public String getFullName(){
        return getName() + " " + Utility.asRomanNumber(getLevel());
    }
}
