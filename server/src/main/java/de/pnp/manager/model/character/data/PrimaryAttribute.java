package de.pnp.manager.model.character.data;

import de.pnp.manager.main.LanguageUtility;
import de.pnp.manager.model.interfaces.WithUnlocalizedName;
import javafx.beans.property.ReadOnlyStringProperty;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum PrimaryAttribute implements IPrimaryAttribute, WithUnlocalizedName {
    STRENGTH("primaryAttribute.strength"), ENDURANCE("primaryAttribute.endurance"),
    DEXTERITY("primaryAttribute.dexterity"), INTELLIGENCE("primaryAttribute.intelligence"),
    CHARISMA("primaryAttribute.charisma"), RESILIENCE("primaryAttribute.resilience"),
    AGILITY("primaryAttribute.agility"), PRECISION("primaryAttribute.precision"),
    DUMMY("primaryAttribute.dummy");

    private final String unlocalizedName;

    PrimaryAttribute(String unlocalizedName) {
        this.unlocalizedName = unlocalizedName;
    }

    @Override
    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    public String getUnlocalizedShortName() {
        return unlocalizedName + ".short";
    }

    public ReadOnlyStringProperty toShortStringProperty() {
        return LanguageUtility.getMessageProperty(getUnlocalizedShortName());
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
