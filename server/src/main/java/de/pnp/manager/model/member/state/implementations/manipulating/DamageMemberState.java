package de.pnp.manager.model.member.state.implementations.manipulating;

import de.pnp.manager.main.LanguageUtility;
import javafx.beans.property.ReadOnlyStringProperty;
import de.pnp.manager.model.member.data.AttackTypes;
import de.pnp.manager.model.member.interfaces.IBattleMember;
import de.pnp.manager.model.member.state.MemberStateIcon;
import de.pnp.manager.model.member.state.RandomPowerMemberState;
import de.pnp.manager.model.member.state.interfaces.IAttackTypeMemberState;
import de.pnp.manager.model.member.state.interfaces.IManipulatingMemberState;
import de.pnp.manager.model.member.state.interfaces.IRandomPowerMemberState;

public class DamageMemberState extends RandomPowerMemberState implements IManipulatingMemberState, IRandomPowerMemberState, IAttackTypeMemberState {

    protected final AttackTypes type;

    public DamageMemberState(String name, int duration, boolean activeRounder, IBattleMember source, float maxPower, boolean random, AttackTypes type) {
        super(name, MemberStateIcon.DAMAGE, duration, activeRounder, source, maxPower, random);
        this.type = type;
    }

    @Override
    public void apply(IBattleMember member) {
        member.takeDamage(Math.round(getEffectPower()), getType(), false, 0, 0, getSource());
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
