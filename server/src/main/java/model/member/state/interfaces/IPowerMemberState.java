package model.member.state.interfaces;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.FloatProperty;

public interface IPowerMemberState extends IMemberState {

    FloatProperty currentPowerProperty();

    float getMaxPower();

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
