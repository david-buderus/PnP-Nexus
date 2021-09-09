package de.pnp.manager.ui.battle.state;

import de.pnp.manager.model.member.data.AttackTypes;
import de.pnp.manager.model.member.interfaces.IBattleMember;
import de.pnp.manager.model.member.state.interfaces.IMemberState;

public interface MemberStateProducer {

    IMemberState create(String name, int duration, boolean activeRounder, float power, boolean isRandom, AttackTypes type, IBattleMember source);
}
