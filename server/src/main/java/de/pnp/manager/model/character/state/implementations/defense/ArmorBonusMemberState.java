package de.pnp.manager.model.character.state.implementations.defense;

import de.pnp.manager.main.LanguageUtility;
import de.pnp.manager.model.character.IPnPCharacter;
import de.pnp.manager.model.character.PnPCharacter;
import de.pnp.manager.model.character.state.IDefenseMemberState;
import de.pnp.manager.model.character.state.MemberStateIcon;
import de.pnp.manager.model.character.state.PowerMemberState;
import de.pnp.manager.model.character.state.interfaces.IPowerMemberStateImpl;
import javafx.beans.property.ReadOnlyStringProperty;

public class ArmorBonusMemberState extends PowerMemberState implements IDefenseMemberState, IPowerMemberStateImpl {

    public ArmorBonusMemberState(String name, int duration, boolean activeRounder, PnPCharacter source, float maxPower) {
        super(name, MemberStateIcon.ARMOR_BONUS, duration, activeRounder, source, maxPower);
    }

    @Override
    public Integer apply(IPnPCharacter character, Integer input) {
        return input + Math.round(getCurrentPower());
    }

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty("state.effect.armorBonus");
    }
}
