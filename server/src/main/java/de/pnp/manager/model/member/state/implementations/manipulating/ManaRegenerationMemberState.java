package de.pnp.manager.model.member.state.implementations.manipulating;

import de.pnp.manager.main.LanguageUtility;
import javafx.beans.property.ReadOnlyStringProperty;
import de.pnp.manager.model.member.interfaces.IBattleMember;
import de.pnp.manager.model.member.state.MemberStateIcon;
import de.pnp.manager.model.member.state.RandomPowerMemberState;
import de.pnp.manager.model.member.state.interfaces.IManipulatingMemberState;
import de.pnp.manager.model.member.state.interfaces.IRandomPowerMemberState;

public class ManaRegenerationMemberState extends RandomPowerMemberState implements IRandomPowerMemberState, IManipulatingMemberState {

    public ManaRegenerationMemberState(String name, int duration, boolean activeRounder, IBattleMember source, float maxPower, boolean random) {
        super(name, MemberStateIcon.MANA_REGENERATION, duration, activeRounder, source, maxPower, random);
    }

    @Override
    public void apply(IBattleMember member) {
        member.increaseMana(Math.round(getEffectPower()), this.source);
    }

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty("state.effect.manaRegeneration");
    }
}
