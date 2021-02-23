package model.member.state.implementations.initiative;

import javafx.beans.property.ReadOnlyStringProperty;
import manager.LanguageUtility;
import model.member.BattleMember;
import model.member.state.MemberStateIcon;
import model.member.state.PowerMemberState;
import model.member.state.interfaces.IPowerMemberState;
import model.member.state.interfaces.IRelativeInitiativeMemberState;

public class RelativeSlowMemberState extends PowerMemberState implements IPowerMemberState, IRelativeInitiativeMemberState {

    public RelativeSlowMemberState(String name, int duration, boolean activeRounder, BattleMember source, float maxPower) {
        super(name, MemberStateIcon.SLOW, duration, activeRounder, source, maxPower);
    }

    @Override
    public Float apply(BattleMember member, Float input) {
        return input * getCurrentPower();
    }

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty("state.effect.relativeSlow");
    }
}
