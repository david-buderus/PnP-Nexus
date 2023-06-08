package de.pnp.manager.model.character.data;

import de.pnp.manager.main.LanguageUtility;
import de.pnp.manager.model.interfaces.WithToStringProperty;
import javafx.beans.property.ReadOnlyStringProperty;

//Will be deleted in the state update of the player character
@Deprecated
public enum FoodRequirement implements WithToStringProperty {
    none(0), little(1), normal(2), much(3), lot(4), extreme(5);

    private final int value;

    FoodRequirement(int value) {
        this.value = value;
    }

    public int toInteger() {
        return this.value;
    }

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty("foodRequirement." + super.toString());
    }
}