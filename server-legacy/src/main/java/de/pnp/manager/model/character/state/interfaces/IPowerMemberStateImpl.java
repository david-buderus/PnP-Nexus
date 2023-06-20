package de.pnp.manager.model.character.state.interfaces;

import de.pnp.manager.model.character.state.IPowerMemberState;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.FloatProperty;

public interface IPowerMemberStateImpl extends IPowerMemberState, IMemberStateImpl {

    FloatProperty currentPowerProperty();

    default StringBinding getPowerAsString() {
        return Bindings.createStringBinding(() -> {
            if (Math.rint(getCurrentPower()) == getCurrentPower()) {
                return String.valueOf((int) getCurrentPower());
            } else {
                return String.valueOf(getCurrentPower());
            }
        }, currentPowerProperty());
    }

    default float getCurrentPower() {
        return currentPowerProperty().get();
    }

    default void setCurrentPower(float currentPower) {
        this.currentPowerProperty().set(currentPower);
    }

    default void decreaseCurrentPower(float amount) {
        this.setCurrentPower(this.getCurrentPower() - amount);
    }
}
