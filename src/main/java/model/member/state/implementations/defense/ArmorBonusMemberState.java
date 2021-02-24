package model.member.state.implementations.defense;

import javafx.beans.property.ReadOnlyStringProperty;
import manager.LanguageUtility;
import model.member.BattleMember;
import model.member.interfaces.IBattleMember;
import model.member.state.MemberStateIcon;
import model.member.state.PowerMemberState;
import model.member.state.interfaces.IDefenseMemberState;
import model.member.state.interfaces.IPowerMemberState;

public class ArmorBonusMemberState extends PowerMemberState implements IDefenseMemberState, IPowerMemberState {

    public ArmorBonusMemberState(String name, int duration, boolean activeRounder, IBattleMember source, float maxPower) {
        super(name, MemberStateIcon.ARMOR_BONUS, duration, activeRounder, source, maxPower);
    }

    @Override
    public Integer apply(IBattleMember member, Integer input) {
        return input + Math.round(getCurrentPower());
    }

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty("state.effect.armorBonus");
    }
}
