package de.pnp.manager.model.character.state;

import de.pnp.manager.main.LanguageUtility;
import de.pnp.manager.model.character.PnPCharacter;
import de.pnp.manager.model.character.state.interfaces.IMemberStateImpl;

public abstract class ActiveRounderMemberState extends MemberState implements IMemberStateImpl, IActiveRounderMemberState {

    protected final boolean activeRounder;

    public ActiveRounderMemberState(String name, int imageID, int duration, boolean activeRounder, PnPCharacter source) {
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
