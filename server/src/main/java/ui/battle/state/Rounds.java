package ui.battle.state;

import javafx.beans.property.ReadOnlyStringProperty;
import manager.LanguageUtility;
import model.interfaces.WithToStringProperty;

public enum Rounds implements WithToStringProperty {
    rounds, activeRounds;

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty("state.info." + super.toString());
    }
}
