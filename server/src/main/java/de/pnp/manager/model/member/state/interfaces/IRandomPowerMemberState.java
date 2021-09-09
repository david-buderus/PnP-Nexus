package de.pnp.manager.model.member.state.interfaces;

import de.pnp.manager.main.LanguageUtility;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;

public interface IRandomPowerMemberState extends IPowerMemberState, IRandomMemberState, IMemberState {

    @Override
    default StringBinding getPowerAsString() {
        return Bindings.createStringBinding(() -> {
            if (Math.rint(getCurrentPower()) == getCurrentPower()) {
                return (isRandom() ? LanguageUtility.getMessage("state.info.dicePrefix") : "") + (int) getCurrentPower();
            } else {
                return (isRandom() ? LanguageUtility.getMessage("state.info.dicePrefix") : "") + getCurrentPower();
            }
        }, currentPowerProperty());
    }

    default float getEffectPower() {
        if (Math.rint(this.getCurrentPower()) == this.getCurrentPower()) {
            int power = Math.round(this.getCurrentPower());

            return power == 0 ? 0 : isRandom() ? getRandom().nextInt(power) + 1 : power;

        } else {
            // If isRandom returns a a random float between 1 and the currentPower
            return isRandom() ? 1 + getRandom().nextFloat() * (getCurrentPower() - 1) : getCurrentPower();
        }
    }
}
