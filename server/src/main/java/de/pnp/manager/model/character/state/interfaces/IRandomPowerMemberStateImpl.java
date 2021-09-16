package de.pnp.manager.model.character.state.interfaces;

import de.pnp.manager.main.LanguageUtility;
import de.pnp.manager.model.character.state.IRandomPowerMemberState;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;

public interface IRandomPowerMemberStateImpl extends IPowerMemberStateImpl, IRandomMemberStateImpl, IMemberStateImpl, IRandomPowerMemberState {

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
            // If isRandom returns a random float between 1 and the currentPower
            return isRandom() ? 1 + getRandom().nextFloat() * (getCurrentPower() - 1) : getCurrentPower();
        }
    }
}
