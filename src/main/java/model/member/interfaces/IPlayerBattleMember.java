package model.member.interfaces;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public interface IPlayerBattleMember extends IExtendedBattleMember {

    StringProperty occupationProperty();

    default String getOccupation() {
        return occupationProperty().get();
    }

    IntegerProperty ageProperty();

    default int getAge() {
        return ageProperty().get();
    }

    StringProperty historyProperty();

    default String getHistory() {
        return historyProperty().get();
    }
}
