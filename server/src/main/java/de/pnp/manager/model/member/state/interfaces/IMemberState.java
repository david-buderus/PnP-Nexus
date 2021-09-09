package de.pnp.manager.model.member.state.interfaces;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import de.pnp.manager.model.member.interfaces.IBattleMember;

public interface IMemberState {

    String getName();

    int getImageID();

    int getMaxDuration();

    IBattleMember getSource();

    IntegerProperty durationProperty();

    StringProperty durationDisplayProperty();

    default void decreaseDuration(boolean isActiveRound) {
        decreaseDuration(isActiveRound, 1);
    }

    default void decreaseDuration(boolean isActiveRound, int amount) {
        if (!isActiveRound) {
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
