package model.member.state.implementations.initiative;

import javafx.beans.property.ReadOnlyStringProperty;
import manager.LanguageUtility;
import model.member.BattleMember;
import model.member.state.PowerMemberState;
import model.member.state.interfaces.IPowerMemberState;
import model.member.state.interfaces.IRelativeInitiativeMemberState;

public class RelativeSpeedMemberState extends PowerMemberState implements IPowerMemberState, IRelativeInitiativeMemberState {

    public RelativeSpeedMemberState(String name, int duration, boolean activeRounder, BattleMember source, float maxPower) {
        super(name, 6, duration, activeRounder, source, maxPower);
    }

    @Override
    public Float apply(BattleMember member, Float input) {
        return input * getCurrentPower();
    }

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty("state.effect.relativeSpeed");
    }
}
