package model.member.state.implementations.incoming;

import javafx.beans.property.ReadOnlyStringProperty;
import manager.LanguageUtility;
import model.member.BattleMember;
import model.member.interfaces.IBattleMember;
import model.member.state.MemberStateIcon;
import model.member.state.PowerMemberState;
import model.member.state.interfaces.IIncomingDamageMemberState;
import model.member.state.interfaces.IPowerMemberState;

public class ShieldMemberState extends PowerMemberState implements IPowerMemberState, IIncomingDamageMemberState {

    public ShieldMemberState(String name, int duration, boolean activeRounder, IBattleMember source, float maxPower) {
        super(name, MemberStateIcon.SHIELD, duration, activeRounder, source, maxPower);
    }

    @Override
    public Integer apply(IBattleMember member, Integer amount) {
        int newAmount = Math.max(amount - Math.round(getCurrentPower()), 0);

        this.decreaseCurrentPower(amount - newAmount);

        if (this.getCurrentPower() <= 0) {
            this.setDuration(0);
        }

        return newAmount;
    }

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty("state.effect.shield");
    }
}
