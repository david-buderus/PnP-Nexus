package de.pnp.manager.model.character.data;

import de.pnp.manager.main.LanguageUtility;
import de.pnp.manager.model.interfaces.WithToStringProperty;
import javafx.beans.property.ReadOnlyStringProperty;

public enum AttackTypes implements IAttackTypes, WithToStringProperty {
    head, upperBody, legs, arm, ignoreArmor, direct;

    @Override
    public String toString() {
        return toStringProperty().get();
    }

    public ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty("attackTypes." + super.toString());
    }
}
