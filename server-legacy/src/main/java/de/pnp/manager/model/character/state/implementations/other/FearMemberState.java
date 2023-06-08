package de.pnp.manager.model.character.state.implementations.other;

import de.pnp.manager.main.LanguageUtility;
import de.pnp.manager.model.character.PnPCharacter;
import de.pnp.manager.model.character.state.ActiveRounderMemberState;
import de.pnp.manager.model.character.state.IActiveRounderMemberState;
import de.pnp.manager.model.character.state.MemberStateIcon;
import de.pnp.manager.model.character.state.interfaces.IMemberStateImpl;
import javafx.beans.property.ReadOnlyStringProperty;

public class FearMemberState extends ActiveRounderMemberState implements IMemberStateImpl, IActiveRounderMemberState {

    public FearMemberState(String name, int duration, boolean activeRounder, PnPCharacter source) {
        super(name, MemberStateIcon.FEAR, duration, activeRounder, source);
    }

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty("state.effect.fear");
    }
}
