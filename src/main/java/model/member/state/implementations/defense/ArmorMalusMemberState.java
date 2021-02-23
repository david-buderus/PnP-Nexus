package model.member.state.implementations.defense;

import javafx.beans.property.ReadOnlyStringProperty;
import manager.LanguageUtility;
import model.member.BattleMember;
import model.member.state.MemberStateIcon;
import model.member.state.PowerMemberState;
import model.member.state.interfaces.IDefenseMemberState;
import model.member.state.interfaces.IPowerMemberState;

public class ArmorMalusMemberState extends PowerMemberState implements IDefenseMemberState, IPowerMemberState {

    public ArmorMalusMemberState(String name, int duration, boolean activeRounder, BattleMember source, float maxPower) {
        super(name, MemberStateIcon.ARMOR_MALUS, duration, activeRounder, source, maxPower);
    }

    @Override
    public Integer apply(BattleMember member, Integer input) {
        return input - Math.round(getCurrentPower());
    }

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty("state.effect.armorMalus");
    }
}
