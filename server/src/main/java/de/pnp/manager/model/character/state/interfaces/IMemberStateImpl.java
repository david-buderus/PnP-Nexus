package de.pnp.manager.model.character.state.interfaces;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.pnp.manager.model.character.PnPCharacter;
import de.pnp.manager.model.character.state.IMemberState;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

@JsonSerialize(as = IMemberState.class)
public interface IMemberStateImpl extends IMemberState {

    PnPCharacter getSource();

    IntegerProperty durationProperty();

    StringProperty durationDisplayProperty();

    @Override
    default int getDuration() {
        return durationProperty().get();
    }

    default void setDuration(int duration) {
        this.durationProperty().set(duration);
    }

    @Override
    default String getSourceID() {
        return getSource().getName(); //TODO : CHANGE TO ID
    }
}
