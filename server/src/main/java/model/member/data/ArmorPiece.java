package model.member.data;

import javafx.beans.property.ReadOnlyStringProperty;
import manager.LanguageUtility;
import model.interfaces.WithToStringProperty;

import java.util.NoSuchElementException;

public enum ArmorPiece implements WithToStringProperty {
    head, upperBody, legs, arm, shield;

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty("armorPiece." + super.toString());
    }

    public static ArmorPiece getArmorPiece(String name) {
        for (ArmorPiece piece : values()) {
            if (piece.toStringProperty().get().equalsIgnoreCase(name.trim())) {
                return piece;
            }
        }
        throw new NoSuchElementException("The ArmorPiece with the name " + name + " does not exists.");
    }
}
