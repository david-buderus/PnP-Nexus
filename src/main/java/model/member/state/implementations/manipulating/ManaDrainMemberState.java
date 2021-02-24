package model.member.state.implementations.manipulating;

import javafx.beans.property.ReadOnlyStringProperty;
import manager.LanguageUtility;
import model.member.BattleMember;
import model.member.interfaces.IBattleMember;
import model.member.state.MemberStateIcon;
import model.member.state.RandomPowerMemberState;
import model.member.state.interfaces.IManipulatingMemberState;
import model.member.state.interfaces.IRandomMemberState;

public class ManaDrainMemberState extends RandomPowerMemberState implements IRandomMemberState, IManipulatingMemberState {

    public ManaDrainMemberState(String name, int duration, boolean activeRounder, IBattleMember source, float maxPower, boolean random) {
        super(name, MemberStateIcon.MANA_DRAIN, duration, activeRounder, source, maxPower, random);
    }

    @Override
    public void apply(IBattleMember member) {
        member.decreaseMana(member.getMana() - Math.round(getEffectPower()), this.source);
    }

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty("state.effect.manaDrain");
    }
}
