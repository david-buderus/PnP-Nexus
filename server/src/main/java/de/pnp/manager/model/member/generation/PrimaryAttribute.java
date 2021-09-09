package de.pnp.manager.model.member.generation;

import de.pnp.manager.main.LanguageUtility;
import javafx.beans.property.ReadOnlyStringProperty;
import de.pnp.manager.model.attribute.IPrimaryAttribute;
import de.pnp.manager.model.interfaces.WithToStringProperty;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum PrimaryAttribute implements IPrimaryAttribute, WithToStringProperty {
    strength, endurance, dexterity, intelligence, charisma, resilience, agility, precision, DUMMY;

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty("primaryAttribute." + super.toString());
    }

    public ReadOnlyStringProperty toShortStringProperty() {
        return LanguageUtility.getMessageProperty("primaryAttribute." + super.toString() + ".short");
    }

    @Override
    public String toShortString() {
        return toShortStringProperty().get();
    }

    public static PrimaryAttribute getPrimaryAttribute(String name) {
        for (PrimaryAttribute attribute : values()) {
            if (attribute.toStringProperty().get().equalsIgnoreCase(name.trim())) {
                return attribute;
            }
        }
        throw new NoSuchElementException("The PrimaryAttribute with the name " + name + " does not exists.");
    }

    public static PrimaryAttribute getPrimaryAttributeOrElse(String name, PrimaryAttribute fallback) {
        try {
            return getPrimaryAttribute(name);
        } catch (NoSuchElementException e) {
            return fallback;
        }
    }

    public static PrimaryAttribute[] getValuesWithoutDummy() {
        return Arrays.stream(PrimaryAttribute.values()).filter(primaryAttribute -> !(primaryAttribute.equals(PrimaryAttribute.DUMMY))).toArray(PrimaryAttribute[]::new);
    }
}
