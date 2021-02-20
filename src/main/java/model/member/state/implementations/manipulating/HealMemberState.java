package model.member.state.implementations.manipulating;

import javafx.beans.property.ReadOnlyStringProperty;
import manager.LanguageUtility;
import model.member.BattleMember;
import model.member.state.RandomPowerMemberState;
import model.member.state.interfaces.IManipulatingMemberState;
import model.member.state.interfaces.IRandomPowerMemberState;

public class HealMemberState extends RandomPowerMemberState implements IManipulatingMemberState, IRandomPowerMemberState {

    public HealMemberState(String name, int duration, boolean activeRounder, BattleMember source, float maxPower, boolean random) {
        super(name, 2, duration, activeRounder, source, maxPower, random);
    }

    @Override
    public void apply(BattleMember member) {
        member.heal(Math.round(getEffectPower()), getSource());
    }

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty("state.effect.heal");
    }
}
