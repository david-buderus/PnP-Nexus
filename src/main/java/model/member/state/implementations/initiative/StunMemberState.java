package model.member.state.implementations.initiative;

import javafx.beans.property.ReadOnlyStringProperty;
import manager.LanguageUtility;
import model.member.interfaces.IBattleMember;
import model.member.state.MemberState;
import model.member.state.MemberStateIcon;
import model.member.state.interfaces.IRelativeInitiativeMemberState;

public class StunMemberState extends MemberState implements IRelativeInitiativeMemberState {

    public StunMemberState(String name, int duration, IBattleMember source) {
        super(name, MemberStateIcon.STUN, duration, source);
    }

    @Override
    public Float apply(IBattleMember member, Float currentInitiative) {
        return 0f;
    }

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty("state.effect.stun");
    }
}
