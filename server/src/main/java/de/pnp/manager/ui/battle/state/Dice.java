package de.pnp.manager.ui.battle.state;

import de.pnp.manager.main.LanguageUtility;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import de.pnp.manager.model.interfaces.WithToStringProperty;

public enum Dice implements WithToStringProperty {
    with, withOut;

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        if (this == with) {
            return LanguageUtility.getMessageProperty("state.info.dicePrefix");
        } else {
            return new ReadOnlyStringWrapper("");
        }
    }
}
