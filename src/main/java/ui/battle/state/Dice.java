package ui.battle.state;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import manager.LanguageUtility;
import model.interfaces.WithToStringProperty;

public enum Dice implements WithToStringProperty {
    with, withOut;

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        if (this == with) {
            return LanguageUtility.getMessageProperty("state.info.dicePrefix");
        }
        else {
            return new ReadOnlyStringWrapper("");
        }
    }
}
