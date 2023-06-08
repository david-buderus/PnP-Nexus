package de.pnp.manager.ui.battle.state;

import de.pnp.manager.main.LanguageUtility;
import de.pnp.manager.model.interfaces.WithToStringProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;

public enum Dice implements WithToStringProperty {
    WITH, WITHOUT;

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        if (this == WITH) {
            return LanguageUtility.getMessageProperty("state.info.dicePrefix");
        } else {
            return new ReadOnlyStringWrapper("");
        }
    }
}
