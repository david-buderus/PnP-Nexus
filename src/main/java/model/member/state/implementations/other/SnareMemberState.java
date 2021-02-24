package model.member.state.implementations.other;

import javafx.beans.property.ReadOnlyStringProperty;
import manager.LanguageUtility;
import model.member.BattleMember;
import model.member.interfaces.IBattleMember;
import model.member.state.ActiveRounderMemberState;
import model.member.state.MemberStateIcon;
import model.member.state.interfaces.IActiveRounderMemberState;

public class SnareMemberState extends ActiveRounderMemberState implements IActiveRounderMemberState {

    public SnareMemberState(String name, int duration, boolean activeRounder, IBattleMember source) {
        super(name, MemberStateIcon.SNARE, duration, activeRounder, source);
    }

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty("state.effect.snare");
    }
}
