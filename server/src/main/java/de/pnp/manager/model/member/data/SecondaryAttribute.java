package de.pnp.manager.model.member.data;

import de.pnp.manager.main.LanguageUtility;
import de.pnp.manager.model.interfaces.WithToStringProperty;
import javafx.beans.property.ReadOnlyStringProperty;

import java.util.NoSuchElementException;

public enum SecondaryAttribute implements ISecondaryAttribute, WithToStringProperty {
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
