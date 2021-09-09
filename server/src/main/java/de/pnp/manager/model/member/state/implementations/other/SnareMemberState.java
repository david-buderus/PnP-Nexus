package de.pnp.manager.model.member.state.implementations.other;

import de.pnp.manager.main.LanguageUtility;
import javafx.beans.property.ReadOnlyStringProperty;
import de.pnp.manager.model.member.interfaces.IBattleMember;
import de.pnp.manager.model.member.state.ActiveRounderMemberState;
import de.pnp.manager.model.member.state.MemberStateIcon;
import de.pnp.manager.model.member.state.interfaces.IActiveRounderMemberState;

public class SnareMemberState extends ActiveRounderMemberState implements IActiveRounderMemberState {

    public SnareMemberState(String name, int duration, boolean activeRounder, IBattleMember source) {
        super(name, MemberStateIcon.SNARE, duration, activeRounder, source);
    }

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty("state.effect.snare");
    }
}
