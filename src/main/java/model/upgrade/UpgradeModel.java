package model.upgrade;

import manager.Utility;

public class UpgradeModel {
    private String name;
    private String target;
    private int slots;
    private String cost;
    private String mana;
    private String effect;
    private String[] materialList = new String[4];
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

    public String getMaterial1(){
        return materialList[0];
    }

    public String getMaterial2(){
        return materialList[1];
    }

    public String getMaterial3(){
        return materialList[2];
    }

    public String getMaterial4(){
        return materialList[3];
    }

    public void setMaterial1(String material){
        this.materialList[0] = material;
    }

    public void setMaterial2(String material){
        this.materialList[1] = material;
    }

    public void setMaterial3(String material){
        this.materialList[2] = material;
    }

    public void setMaterial4(String material){
        this.materialList[3] = material;
    }

    public void setMaterial(int i, String material){
        this.materialList[i] = material;
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
