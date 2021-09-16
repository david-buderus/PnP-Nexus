package de.pnp.manager.model.character.state.implementations.incoming;

import de.pnp.manager.main.LanguageUtility;
import de.pnp.manager.model.character.IPnPCharacter;
import de.pnp.manager.model.character.PnPCharacter;
import de.pnp.manager.model.character.state.IIncomingDamageMemberState;
import de.pnp.manager.model.character.state.MemberStateIcon;
import de.pnp.manager.model.character.state.PowerMemberState;
import de.pnp.manager.model.character.state.interfaces.IPowerMemberStateImpl;
import javafx.beans.property.ReadOnlyStringProperty;

public class ShieldMemberState extends PowerMemberState implements IPowerMemberStateImpl, IIncomingDamageMemberState {

    public ShieldMemberState(String name, int duration, boolean activeRounder, PnPCharacter source, float maxPower) {
        super(name, MemberStateIcon.SHIELD, duration, activeRounder, source, maxPower);
    }

    @Override
    public Integer apply(IPnPCharacter character, Integer amount) {
        int newAmount = Math.max(amount - Math.round(getCurrentPower()), 0);

        this.decreaseCurrentPower(amount - newAmount);

        if (this.getCurrentPower() <= 0) {
            this.setDuration(0);
        }

        return newAmount;
    }

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty("state.effect.shield");
    }
}
