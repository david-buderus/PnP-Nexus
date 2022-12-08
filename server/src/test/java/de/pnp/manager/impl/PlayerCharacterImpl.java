package de.pnp.manager.impl;

import de.pnp.manager.model.ICurrency;
import de.pnp.manager.model.character.IPlayerCharacter;

public class PlayerCharacterImpl extends PnPCharacterImpl implements IPlayerCharacter {
    
    protected String race;
    protected String gender;
    protected String age;
    protected int experience;
    protected String profession;
    protected ICurrency currency;
    protected String history;

    @Override
    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    @Override
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    @Override
    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    @Override
    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    @Override
    public ICurrency getCurrency() {
        return currency;
    }

    public void setCurrency(ICurrency currency) {
        this.currency = currency;
    }

    @Override
    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }
}
