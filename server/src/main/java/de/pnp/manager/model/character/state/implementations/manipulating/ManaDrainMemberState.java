package de.pnp.manager.model.character.state.implementations.manipulating;

import de.pnp.manager.main.LanguageUtility;
import de.pnp.manager.model.character.IPnPCharacter;
import de.pnp.manager.model.character.PnPCharacter;
import de.pnp.manager.model.character.state.IManipulatingMemberState;
import de.pnp.manager.model.character.state.MemberStateIcon;
import de.pnp.manager.model.character.state.RandomPowerMemberState;
import de.pnp.manager.model.character.state.interfaces.IRandomMemberStateImpl;
import javafx.beans.property.ReadOnlyStringProperty;

public class ManaDrainMemberState extends RandomPowerMemberState implements IRandomMemberStateImpl, IManipulatingMemberState {

    public ManaDrainMemberState(String name, int duration, boolean activeRounder, PnPCharacter source, float maxPower, boolean random) {
        super(name, MemberStateIcon.MANA_DRAIN, duration, activeRounder, source, maxPower, random);
    }

    @Override
    public void apply(IPnPCharacter character) {
        ((PnPCharacter) character).decreaseMana(Math.round(getEffectPower()), this.source);
    }

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty("state.effect.manaDrain");
    }
}
