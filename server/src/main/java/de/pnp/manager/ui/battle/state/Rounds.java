package de.pnp.manager.ui.battle.state;

import de.pnp.manager.main.LanguageUtility;
import javafx.beans.property.ReadOnlyStringProperty;
import de.pnp.manager.model.interfaces.WithToStringProperty;

public enum Rounds implements WithToStringProperty {
    rounds, activeRounds;

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty("state.info." + super.toString());
    }
}
