package model.member.state.implementations.manipulating;

import javafx.beans.property.ReadOnlyStringProperty;
import manager.LanguageUtility;
import model.member.interfaces.IBattleMember;
import model.member.state.MemberStateIcon;
import model.member.state.RandomPowerMemberState;
import model.member.state.interfaces.IManipulatingMemberState;
import model.member.state.interfaces.IRandomPowerMemberState;

public class HealMemberState extends RandomPowerMemberState implements IManipulatingMemberState, IRandomPowerMemberState {

    public HealMemberState(String name, int duration, boolean activeRounder, IBattleMember source, float maxPower, boolean random) {
        super(name, MemberStateIcon.HEAL, duration, activeRounder, source, maxPower, random);
    }

    @Override
    public void apply(IBattleMember member) {
        member.heal(Math.round(getEffectPower()), getSource());
    }

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty("state.effect.heal");
    }
}
