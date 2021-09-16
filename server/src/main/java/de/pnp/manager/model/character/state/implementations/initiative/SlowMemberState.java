package de.pnp.manager.model.character.state.implementations.initiative;

import de.pnp.manager.main.LanguageUtility;
import de.pnp.manager.model.character.IPnPCharacter;
import de.pnp.manager.model.character.PnPCharacter;
import de.pnp.manager.model.character.state.IAbsolutInitiativeMemberState;
import de.pnp.manager.model.character.state.MemberStateIcon;
import de.pnp.manager.model.character.state.PowerMemberState;
import de.pnp.manager.model.character.state.interfaces.IPowerMemberStateImpl;
import javafx.beans.property.ReadOnlyStringProperty;

public class SlowMemberState extends PowerMemberState implements IPowerMemberStateImpl, IAbsolutInitiativeMemberState {

    public SlowMemberState(String name, int duration, boolean activeRounder, PnPCharacter source, float maxPower) {
        super(name, MemberStateIcon.SLOW, duration, activeRounder, source, maxPower);
    }

    @Override
    public Integer apply(IPnPCharacter character, Integer input) {
        return input - Math.round(getCurrentPower());
    }

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty("state.effect.slow");
    }
}
