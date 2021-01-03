package model;

import javafx.beans.property.ReadOnlyStringProperty;
import manager.LanguageUtility;
import manager.Utility;
import model.interfaces.WithToStringProperty;

import java.util.NoSuchElementException;

public enum Rarity implements WithToStringProperty {
    unknown, common, rare, epic, legendary, godlike;

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty("rarity." + super.toString());
    }

    public float getChance() {
        if (this == unknown) {
            return Float.MAX_VALUE;
        }
        return Utility.getConfig().getFloat("rarity.chance." + super.toString());
    }

    @Override
    public String toString() {
        return toStringProperty().get();
    }

    public static Rarity getRarity(String name) {
        for (Rarity rarity : values()) {
            if (rarity.toStringProperty().get().equalsIgnoreCase(name.trim())) {
                return rarity;
            }
        }
        throw new NoSuchElementException("The Rarity with the name " + name + " does not exists.");
    }
}
