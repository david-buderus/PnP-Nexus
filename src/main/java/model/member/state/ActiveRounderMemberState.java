package model.member.state;

import manager.LanguageUtility;
import model.member.BattleMember;
import model.member.interfaces.IBattleMember;
import model.member.state.interfaces.IActiveRounderMemberState;

public abstract class ActiveRounderMemberState extends MemberState implements IActiveRounderMemberState {

    protected final boolean activeRounder;

    public ActiveRounderMemberState(String name, int imageID, int duration, boolean activeRounder, IBattleMember source) {
        super(name, imageID, duration, source);
        this.activeRounder = activeRounder;
        this.durationDisplay.unbind();
        this.durationDisplay.bind(this.duration.asString().concat(" ").concat(this.activeRounder ?
                LanguageUtility.getMessageProperty("state.info.activeRounds") :
                LanguageUtility.getMessageProperty("state.info.rounds")));
    }

    @Override
    public boolean isActiveRounder() {
        return this.activeRounder;
    }
}
