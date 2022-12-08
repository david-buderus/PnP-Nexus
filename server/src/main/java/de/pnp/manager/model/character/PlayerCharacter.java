package de.pnp.manager.model.character;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.pnp.manager.model.Battle;
import de.pnp.manager.model.Currency;
import de.pnp.manager.model.ICurrency;
import de.pnp.manager.model.loot.ILootTable;
import de.pnp.manager.model.loot.LootTable;
import javafx.beans.property.*;

@JsonSerialize(as = IPlayerCharacter.class)
public class PlayerCharacter extends PnPCharacter implements IPlayerCharacter {

    protected StringProperty race;
    protected StringProperty age;
    protected IntegerProperty experience;
    protected StringProperty profession;
    protected ObjectProperty<ICurrency> currency;
    protected StringProperty history;
    protected StringProperty gender;

    public PlayerCharacter(String characterID, Battle battle) {
        this(characterID, battle, new LootTable());
    }

    public PlayerCharacter(String characterID, Battle battle, ILootTable lootTable) {
        super(characterID, battle, lootTable);
        this.race = new SimpleStringProperty("");
        this.age = new SimpleStringProperty("");
        this.experience = new SimpleIntegerProperty(0);
        this.profession = new SimpleStringProperty("");
        this.currency = new SimpleObjectProperty<>(new Currency(0));
        this.history = new SimpleStringProperty("");
        this.gender = new SimpleStringProperty("");
    }

    @Override
    public ILootTable getFinishedLootTable() {
        ILootTable result = super.getFinishedLootTable();
        result.add(getCurrency());

        return result;
    }

    @Override
    public String getRace() {
        return race.get();
    }

    @Override
    public String getGender() {
        return gender.get();
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public StringProperty raceProperty() {
        return race;
    }

    public void setRace(String race) {
        this.race.set(race);
    }

    @Override
    public String getAge() {
        return age.get();
    }

    public StringProperty ageProperty() {
        return age;
    }

    public void setAge(String age) {
        this.age.set(age);
    }

    @Override
    public int getExperience() {
        return experience.get();
    }

    public IntegerProperty experienceProperty() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience.set(experience);
    }

    @Override
    public String getProfession() {
        return profession.get();
    }

    public StringProperty professionProperty() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession.set(profession);
    }

    @Override
    public ICurrency getCurrency() {
        return currency.get();
    }

    public ObjectProperty<ICurrency> currencyProperty() {
        return currency;
    }

    public void setCurrency(ICurrency currency) {
        this.currency.set(currency);
    }

    @Override
    public String getHistory() {
        return history.get();
    }

    public StringProperty historyProperty() {
        return history;
    }

    public void setHistory(String history) {
        this.history.set(history);
    }
}
