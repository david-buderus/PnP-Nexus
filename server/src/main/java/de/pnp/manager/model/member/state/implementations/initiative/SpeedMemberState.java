package de.pnp.manager.model.member.state.implementations.initiative;

import de.pnp.manager.main.LanguageUtility;
import javafx.beans.property.ReadOnlyStringProperty;
import de.pnp.manager.model.member.interfaces.IBattleMember;
import de.pnp.manager.model.member.state.MemberStateIcon;
import de.pnp.manager.model.member.state.PowerMemberState;
import de.pnp.manager.model.member.state.interfaces.IAbsolutInitiativeMemberState;
import de.pnp.manager.model.member.state.interfaces.IPowerMemberState;

public class SpeedMemberState extends PowerMemberState implements IPowerMemberState, IAbsolutInitiativeMemberState {

    public SpeedMemberState(String name, int duration, boolean activeRounder, IBattleMember source, float maxPower) {
        super(name, MemberStateIcon.SPEED, duration, activeRounder, source, maxPower);
    }

    @Override
    public Integer apply(IBattleMember member, Integer input) {
        return input + Math.round(getCurrentPower());
    }

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty("state.effect.speed");
    }
}
