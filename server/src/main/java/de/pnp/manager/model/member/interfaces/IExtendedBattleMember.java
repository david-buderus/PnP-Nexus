package de.pnp.manager.model.member.interfaces;

import de.pnp.manager.model.interfaces.ILootable;
import de.pnp.manager.model.item.Armor;
import de.pnp.manager.model.item.Jewellery;
import de.pnp.manager.model.item.Weapon;
import de.pnp.manager.model.member.data.IPrimaryAttribute;
import de.pnp.manager.model.member.data.SecondaryAttribute;
import de.pnp.manager.model.other.Spell;
import de.pnp.manager.model.other.Talent;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.util.Collection;

public interface IExtendedBattleMember extends IBattleMember, ILootable {
    IntegerProperty getTalent(Talent talent);

    Collection<Talent> getTalents();

    IntegerProperty getAttribute(IPrimaryAttribute attribute);

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

    boolean dropsWeapons();

    boolean dropsArmor();

    boolean dropsJewellery();
}
