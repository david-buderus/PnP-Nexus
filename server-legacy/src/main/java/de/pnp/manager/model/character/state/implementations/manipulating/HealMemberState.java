package de.pnp.manager.model.character.state.implementations.manipulating;

import de.pnp.manager.main.LanguageUtility;
import de.pnp.manager.model.character.IPnPCharacter;
import de.pnp.manager.model.character.PnPCharacter;
import de.pnp.manager.model.character.state.IManipulatingMemberState;
import de.pnp.manager.model.character.state.MemberStateIcon;
import de.pnp.manager.model.character.state.RandomPowerMemberState;
import de.pnp.manager.model.character.state.interfaces.IRandomPowerMemberStateImpl;
import javafx.beans.property.ReadOnlyStringProperty;

public class HealMemberState extends RandomPowerMemberState implements IManipulatingMemberState, IRandomPowerMemberStateImpl {

    public HealMemberState(String name, int duration, boolean activeRounder, PnPCharacter source, float maxPower, boolean random) {
        super(name, MemberStateIcon.HEAL, duration, activeRounder, source, maxPower, random);
    }

    @Override
    public void apply(IPnPCharacter character) {
        ((PnPCharacter) character).heal(Math.round(getEffectPower()), getSource());
    }

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty("state.effect.heal");
    }
}
