package model.member.state.implementations.initiative;

import javafx.beans.property.ReadOnlyStringProperty;
import manager.LanguageUtility;
import model.member.interfaces.IBattleMember;
import model.member.state.MemberStateIcon;
import model.member.state.PowerMemberState;
import model.member.state.interfaces.IPowerMemberState;
import model.member.state.interfaces.IRelativeInitiativeMemberState;

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
