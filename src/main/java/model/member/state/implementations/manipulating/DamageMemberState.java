package model.member.state.implementations.manipulating;

import javafx.beans.property.ReadOnlyStringProperty;
import manager.LanguageUtility;
import model.member.BattleMember;
import model.member.data.AttackTypes;
import model.member.state.MemberStateIcon;
import model.member.state.RandomPowerMemberState;
import model.member.state.interfaces.IAttackTypeMemberState;
import model.member.state.interfaces.IManipulatingMemberState;
import model.member.state.interfaces.IRandomPowerMemberState;

public class DamageMemberState extends RandomPowerMemberState implements IManipulatingMemberState, IRandomPowerMemberState, IAttackTypeMemberState {

    protected final AttackTypes type;

    public DamageMemberState(String name, int duration, boolean activeRounder, BattleMember source, float maxPower, boolean random, AttackTypes type) {
        super(name, MemberStateIcon.DAMAGE, duration, activeRounder, source, maxPower, random);
        this.type = type;
    }

    @Override
    public void apply(BattleMember member) {
        member.takeDamage(Math.round(getEffectPower()), getType(), false, 0, 0, getSource());
    }

    @Override
    public AttackTypes getType() {
        return type;
    }

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty("state.effect.damage");
    }
}
