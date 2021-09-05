package model.member.interfaces;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import model.Spell;
import model.item.Armor;
import model.item.Jewellery;
import model.item.Weapon;
import model.member.generation.PrimaryAttribute;
import model.member.generation.SecondaryAttribute;
import model.member.generation.Talent;

import java.util.Collection;

public interface IExtendedBattleMember extends IBattleMember {
    IntegerProperty getTalent(Talent talent);

    Collection<Talent> getMainTalents();

    IntegerProperty getAttribute(PrimaryAttribute attribute);

    ReadOnlyIntegerProperty getAttribute(SecondaryAttribute attribute);

    IntegerProperty getModifier(SecondaryAttribute attribute);

    ObservableList<Weapon> getWeapons();

    ObservableList<Armor> getArmor();

    ObservableList<Jewellery> getJewelleries();

    ObservableList<Spell> getSpells();

    default int getStrength() {
        return this.strengthProperty().get();
    }

    IntegerProperty strengthProperty();

    default int getEndurance() {
        return this.enduranceProperty().get();
    }

    IntegerProperty enduranceProperty();

    default int getDexterity() {
        return this.dexterityProperty().get();
    }

    IntegerProperty dexterityProperty();

    default int getIntelligence() {
        return this.intelligenceProperty().get();
    }

    IntegerProperty intelligenceProperty();

    default int getCharisma() {
        return this.charismaProperty().get();
    }

    IntegerProperty charismaProperty();

    default int getResilience() {
        return this.resilienceProperty().get();
    }

    IntegerProperty resilienceProperty();

    default int getAgility() {
        return this.agilityProperty().get();
    }

    IntegerProperty agilityProperty();

    default int getPrecision() {
        return this.precisionProperty().get();
    }

    IntegerProperty precisionProperty();

    default String getNotes() {
        return this.notesProperty().get();
    }

    StringProperty notesProperty();
}
