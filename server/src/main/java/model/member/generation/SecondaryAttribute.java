package model.member.generation;

import javafx.beans.property.ReadOnlyStringProperty;
import manager.LanguageUtility;
import model.interfaces.WithToStringProperty;

import java.util.NoSuchElementException;

public enum SecondaryAttribute implements WithToStringProperty {
    meleeDamage, rangeDamage, magicPower, defense, initiative, health, mentalHealth, mana;

    public static SecondaryAttribute getSecondaryAttribute(String name) {
        for (SecondaryAttribute attribute : values()) {
            if (attribute.toStringProperty().get().equalsIgnoreCase(name.trim())) {
                return attribute;
            }
        }
        throw new NoSuchElementException("The SecondaryAttribute with the name " + name + " does not exists.");
    }

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty("secondaryAttribute." + super.toString());
    }
}
