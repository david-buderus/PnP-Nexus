package model.member.interfaces;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import model.loot.LootTable;
import model.member.BattleMember;
import model.member.data.ArmorPiece;
import model.member.data.AttackTypes;
import model.member.state.interfaces.IMemberState;

public interface IBattleMember extends IMember {
    void nextTurn();
    void heal(int amount, IBattleMember source);
    void takeDamage(int amount, AttackTypes type, boolean withShield, double penetration,
        double block, IBattleMember source);

    default void decreaseMana(int amount, IBattleMember source) {
        this.manaProperty().set(Math.min(0, this.getMana() - amount));
    }

    default void increaseMana(int amount, IBattleMember source) {
        this.manaProperty().set(Math.min(this.getMaxMana(), this.getMana() + amount));
    }
    void reset();

    void addState(IMemberState state);
    void removeState(IMemberState state);
    LootTable getLootTable();

    boolean isDead();

    int getArmor(ArmorPiece target);
    IntegerProperty armorProperty(ArmorPiece piece);

    void applyWearOnWeapons();

    BattleMember cloneMember();

    int getLife();
    IntegerProperty lifeProperty();

    int getMaxLife();
    IntegerProperty maxLifeProperty();

    int getMaxMana();
    IntegerProperty maxManaProperty();

    int getMana();
    IntegerProperty manaProperty();

    int getInitiative();
    IntegerProperty initiativeProperty();

    int getStartValue();
    IntegerProperty startValueProperty();

    int getCounter();
    IntegerProperty counterProperty();

    int getTurns();
    IntegerProperty turnsProperty();

    ListProperty<IMemberState> statesProperty();
    IntegerProperty baseDefenseProperty();

    int getLevel();
    IntegerProperty levelProperty();

    default int getTier() {
        return (int) Math.ceil(this.getLevel() / 5f);
    }

}