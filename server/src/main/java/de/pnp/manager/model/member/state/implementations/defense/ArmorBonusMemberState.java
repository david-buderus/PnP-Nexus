package de.pnp.manager.model.member.state.implementations.defense;

import de.pnp.manager.main.LanguageUtility;
import javafx.beans.property.ReadOnlyStringProperty;
import de.pnp.manager.model.member.interfaces.IBattleMember;
import de.pnp.manager.model.member.state.MemberStateIcon;
import de.pnp.manager.model.member.state.PowerMemberState;
import de.pnp.manager.model.member.state.interfaces.IDefenseMemberState;
import de.pnp.manager.model.member.state.interfaces.IPowerMemberState;

public class ArmorBonusMemberState extends PowerMemberState implements IDefenseMemberState, IPowerMemberState {

    public ArmorBonusMemberState(String name, int duration, boolean activeRounder, IBattleMember source, float maxPower) {
        super(name, MemberStateIcon.ARMOR_BONUS, duration, activeRounder, source, maxPower);
    }

    @Override
    public Integer apply(IBattleMember member, Integer input) {
        return input + Math.round(getCurrentPower());
    }

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty("state.effect.armorBonus");
    }
}
