package model.member.interfaces;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
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
        this.manaProperty().set(Math.max(0, this.getMana() - amount));
    }

    default void increaseMana(int amount, IBattleMember source) {
        this.manaProperty().set(Math.min(this.getMaxMana(), this.getMana() + amount));
    }

    void reset();

    void addState(IMemberState state);

    void removeState(IMemberState state);

    default boolean isDead() {
        return getLife() <= 0;
    }

    default int getArmor(ArmorPiece piece) {
        return this.armorProperty(piece).get();
    }

    IntegerProperty armorProperty(ArmorPiece piece);

    void applyWearOnWeapons();

    BattleMember cloneMember();

    default int getLife() {
        return this.lifeProperty().get();
    }

    IntegerProperty lifeProperty();

    default int getMaxLife() {
        return this.maxLifeProperty().get();
    }

    IntegerProperty maxLifeProperty();

    default int getMaxMana() {
        return this.maxManaProperty().get();
    }

    IntegerProperty maxManaProperty();

    default int getMana() {
        return this.manaProperty().get();
    }

    IntegerProperty manaProperty();

    default int getInitiative() {
        return this.initiativeProperty().get();
    }

    IntegerProperty initiativeProperty();

    default int getStartValue() {
        return this.startValueProperty().get();
    }

    IntegerProperty startValueProperty();

    default int getCounter() {
        return this.counterProperty().get();
    }

    IntegerProperty counterProperty();

    default int getTurns() {
        return turnsProperty().get();
    }

    IntegerProperty turnsProperty();

    ListProperty<IMemberState> statesProperty();

    IntegerProperty baseDefenseProperty();

    default int getLevel() {
        return levelProperty().get();
    }

    IntegerProperty levelProperty();

    default int getTier() {
        return (int) Math.ceil(this.getLevel() / 5f);
    }


}