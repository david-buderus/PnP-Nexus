package model.member.state.implementations.other;

import javafx.beans.property.ReadOnlyStringProperty;
import manager.LanguageUtility;
import model.member.interfaces.IBattleMember;
import model.member.state.ActiveRounderMemberState;
import model.member.state.MemberStateIcon;
import model.member.state.interfaces.IActiveRounderMemberState;

public class FearMemberState extends ActiveRounderMemberState implements IActiveRounderMemberState {

    public FearMemberState(String name, int duration, boolean activeRounder, IBattleMember source) {
        super(name, MemberStateIcon.FEAR, duration, activeRounder, source);
    }

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty("state.effect.fear");
    }
}
