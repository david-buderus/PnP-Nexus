package de.pnp.manager.model.member.state.implementations.manipulating;

import de.pnp.manager.main.LanguageUtility;
import javafx.beans.property.ReadOnlyStringProperty;
import de.pnp.manager.model.member.interfaces.IBattleMember;
import de.pnp.manager.model.member.state.MemberStateIcon;
import de.pnp.manager.model.member.state.RandomPowerMemberState;
import de.pnp.manager.model.member.state.interfaces.IManipulatingMemberState;
import de.pnp.manager.model.member.state.interfaces.IRandomMemberState;

public class ManaDrainMemberState extends RandomPowerMemberState implements IRandomMemberState, IManipulatingMemberState {

    public ManaDrainMemberState(String name, int duration, boolean activeRounder, IBattleMember source, float maxPower, boolean random) {
        super(name, MemberStateIcon.MANA_DRAIN, duration, activeRounder, source, maxPower, random);
    }

    @Override
    public void apply(IBattleMember member) {
        member.decreaseMana(Math.round(getEffectPower()), this.source);
    }

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty("state.effect.manaDrain");
    }
}
