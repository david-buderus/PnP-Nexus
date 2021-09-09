package de.pnp.manager.model.member.state.implementations.initiative;

import de.pnp.manager.main.LanguageUtility;
import javafx.beans.property.ReadOnlyStringProperty;
import de.pnp.manager.model.member.interfaces.IBattleMember;
import de.pnp.manager.model.member.state.MemberState;
import de.pnp.manager.model.member.state.MemberStateIcon;
import de.pnp.manager.model.member.state.interfaces.IRelativeInitiativeMemberState;

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
