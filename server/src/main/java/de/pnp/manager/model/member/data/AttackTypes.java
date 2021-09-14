package de.pnp.manager.model.member.data;

import de.pnp.manager.main.LanguageUtility;
import javafx.beans.property.ReadOnlyStringProperty;
import de.pnp.manager.model.interfaces.WithToStringProperty;

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
