package de.pnp.manager.model.member.state.implementations.other;

import de.pnp.manager.main.LanguageUtility;
import javafx.beans.property.ReadOnlyStringProperty;
import de.pnp.manager.model.member.interfaces.IBattleMember;
import de.pnp.manager.model.member.state.ActiveRounderMemberState;
import de.pnp.manager.model.member.state.MemberStateIcon;
import de.pnp.manager.model.member.state.interfaces.IActiveRounderMemberState;

public class FearMemberState extends ActiveRounderMemberState implements IActiveRounderMemberState {

    public FearMemberState(String name, int duration, boolean activeRounder, IBattleMember source) {
        super(name, MemberStateIcon.FEAR, duration, activeRounder, source);
    }

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty("state.effect.fear");
    }
}
