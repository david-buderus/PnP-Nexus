package model.member.state.interfaces;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import model.member.BattleMember;

public interface IMemberState {

    String getName();

    int getImageID();

    int getMaxDuration();

    BattleMember getSource();

    IntegerProperty durationProperty();

    StringProperty durationDisplayProperty();

    default void decreaseDuration(boolean isActiveRoundDecrease) {
        decreaseDuration(isActiveRoundDecrease, 1);
    }

    default void decreaseDuration(boolean isActiveRoundDecrease, int amount) {
        if (!isActiveRoundDecrease) {
            this.setDuration(this.getDuration() - amount);
        }
    }

    default int getDuration() {
        return durationProperty().get();
    }

    default void setDuration(int duration) {
        this.durationProperty().set(duration);
    }
}
