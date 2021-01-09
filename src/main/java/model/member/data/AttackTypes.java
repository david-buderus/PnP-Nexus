package model.member.data;

import javafx.beans.property.ReadOnlyStringProperty;
import manager.LanguageUtility;
import model.interfaces.WithToStringProperty;

public enum AttackTypes implements WithToStringProperty {
    head, upperBody, legs, arm, ignoreArmor, direct;

    @Override
    public String toString() {
        return toStringProperty().get();
    }

    public ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty("attackTypes." + super.toString());
    }
}
