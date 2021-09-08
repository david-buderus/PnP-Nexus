package model.member.data;

import javafx.beans.property.ReadOnlyStringProperty;
import manager.LanguageUtility;
import model.interfaces.WithToStringProperty;

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
