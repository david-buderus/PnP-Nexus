package de.pnp.manager.model.member.data;

import de.pnp.manager.main.LanguageUtility;
import javafx.beans.property.ReadOnlyStringProperty;
import de.pnp.manager.model.interfaces.WithToStringProperty;

import java.util.NoSuchElementException;

public enum ArmorPosition implements IArmorPosition, WithToStringProperty {
    head, upperBody, arm, legs;

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty("armorPosition." + super.toString());
    }

    public static ArmorPosition getArmorPosition(String name) {
        for (ArmorPosition pos : values()) {
            if (pos.toStringProperty().get().equalsIgnoreCase(name.trim())) {
                return pos;
            }
        }
        throw new NoSuchElementException("The ArmorPosition with the name " + name + " does not exists.");
    }
}
