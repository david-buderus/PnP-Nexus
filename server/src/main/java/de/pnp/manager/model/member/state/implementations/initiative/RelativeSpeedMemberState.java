package de.pnp.manager.model.member.state.implementations.initiative;

import de.pnp.manager.main.LanguageUtility;
import javafx.beans.property.ReadOnlyStringProperty;
import de.pnp.manager.model.member.interfaces.IBattleMember;
import de.pnp.manager.model.member.state.MemberStateIcon;
import de.pnp.manager.model.member.state.PowerMemberState;
import de.pnp.manager.model.member.state.interfaces.IPowerMemberState;
import de.pnp.manager.model.member.state.interfaces.IRelativeInitiativeMemberState;

public class RelativeSpeedMemberState extends PowerMemberState implements IPowerMemberState, IRelativeInitiativeMemberState {

    public RelativeSpeedMemberState(String name, int duration, boolean activeRounder, IBattleMember source, float maxPower) {
        super(name, MemberStateIcon.SPEED, duration, activeRounder, source, maxPower);
    }

    @Override
    public Float apply(IBattleMember member, Float input) {
        return input * getCurrentPower();
    }

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty("state.effect.relativeSpeed");
    }
}
