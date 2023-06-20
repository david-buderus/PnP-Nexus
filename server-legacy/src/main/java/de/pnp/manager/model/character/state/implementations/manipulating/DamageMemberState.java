package de.pnp.manager.model.character.state.implementations.manipulating;

import de.pnp.manager.main.LanguageUtility;
import de.pnp.manager.model.character.IPnPCharacter;
import de.pnp.manager.model.character.PnPCharacter;
import de.pnp.manager.model.character.data.AttackTypes;
import de.pnp.manager.model.character.state.IAttackTypeMemberState;
import de.pnp.manager.model.character.state.IManipulatingMemberState;
import de.pnp.manager.model.character.state.MemberStateIcon;
import de.pnp.manager.model.character.state.RandomPowerMemberState;
import de.pnp.manager.model.character.state.interfaces.IMemberStateImpl;
import de.pnp.manager.model.character.state.interfaces.IRandomPowerMemberStateImpl;
import javafx.beans.property.ReadOnlyStringProperty;

public class DamageMemberState extends RandomPowerMemberState implements IManipulatingMemberState, IRandomPowerMemberStateImpl, IMemberStateImpl, IAttackTypeMemberState {

    protected final AttackTypes type;

    public DamageMemberState(String name, int duration, boolean activeRounder, PnPCharacter source, float maxPower, boolean random, AttackTypes type) {
        super(name, MemberStateIcon.DAMAGE, duration, activeRounder, source, maxPower, random);
        this.type = type;
    }

    @Override
    public void apply(IPnPCharacter character) {
        ((PnPCharacter) character).takeDamage(Math.round(getEffectPower()), getType(), false, 0, 0, getSource());
    }

    @Override
    public AttackTypes getType() {
        return type;
    }

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty("state.effect.damage");
    }
}
