package model;

import model.member.generation.Talent;

public class Spell {

    private String name;
    private String effect;
    private String type;
    private String cost;
    private String castTime;
    private Talent talent;
    private int tier;

    public Spell() {
        this.name = "";
        this.effect = "";
        this.type = "";
        this.cost = "";
        this.castTime = "";
        this.talent = new Talent();
        this.tier = 1;
    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getCastTime() {
        return castTime;
    }

    public void setCastTime(String castTime) {
        this.castTime = castTime;
    }

    public Talent getTalent() {
        return talent;
    }

    public void setTalent(Talent talent) {
        this.talent = talent;
    }
}
