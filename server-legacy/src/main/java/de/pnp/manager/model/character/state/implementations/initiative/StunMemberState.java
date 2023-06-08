package de.pnp.manager.model.character.state.implementations.initiative;

import de.pnp.manager.main.LanguageUtility;
import de.pnp.manager.model.character.IPnPCharacter;
import de.pnp.manager.model.character.PnPCharacter;
import de.pnp.manager.model.character.state.IRelativeInitiativeMemberState;
import de.pnp.manager.model.character.state.MemberState;
import de.pnp.manager.model.character.state.MemberStateIcon;
import javafx.beans.property.ReadOnlyStringProperty;

public class StunMemberState extends MemberState implements IRelativeInitiativeMemberState {

    public StunMemberState(String name, int duration, PnPCharacter source) {
        super(name, MemberStateIcon.STUN, duration, source);
    }

    @Override
    public Float apply(IPnPCharacter character, Float currentInitiative) {
        return 0f;
    }

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty("state.effect.stun");
    }
}
