package model.member.state.implementations.initiative;

import javafx.beans.property.ReadOnlyStringProperty;
import manager.LanguageUtility;
import model.member.BattleMember;
import model.member.state.MemberStateIcon;
import model.member.state.PowerMemberState;
import model.member.state.interfaces.IAbsolutInitiativeMemberState;
import model.member.state.interfaces.IPowerMemberState;

public class SpeedMemberState extends PowerMemberState implements IPowerMemberState, IAbsolutInitiativeMemberState {

    public SpeedMemberState(String name, int duration, boolean activeRounder, BattleMember source, float maxPower) {
        super(name, MemberStateIcon.SPEED, duration, activeRounder, source, maxPower);
    }

    @Override
    public Integer apply(BattleMember member, Integer input) {
        return input + Math.round(getCurrentPower());
    }

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty("state.effect.speed");
    }
}
