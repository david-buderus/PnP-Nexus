package de.pnp.manager.model.character.state.implementations.initiative;

import de.pnp.manager.main.LanguageUtility;
import de.pnp.manager.model.character.IPnPCharacter;
import de.pnp.manager.model.character.PnPCharacter;
import de.pnp.manager.model.character.state.IRelativeInitiativeMemberState;
import de.pnp.manager.model.character.state.MemberStateIcon;
import de.pnp.manager.model.character.state.PowerMemberState;
import de.pnp.manager.model.character.state.interfaces.IPowerMemberStateImpl;
import javafx.beans.property.ReadOnlyStringProperty;

public class RelativeSpeedMemberState extends PowerMemberState implements IPowerMemberStateImpl, IRelativeInitiativeMemberState {

    public RelativeSpeedMemberState(String name, int duration, boolean activeRounder, PnPCharacter source, float maxPower) {
        super(name, MemberStateIcon.SPEED, duration, activeRounder, source, maxPower);
    }

    @Override
    public Float apply(IPnPCharacter character, Float input) {
        return input * getCurrentPower();
    }

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty("state.effect.relativeSpeed");
    }
}
