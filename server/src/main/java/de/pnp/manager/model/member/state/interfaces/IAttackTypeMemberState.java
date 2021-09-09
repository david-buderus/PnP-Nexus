package de.pnp.manager.model.member.state.interfaces;

import de.pnp.manager.model.member.data.AttackTypes;

public interface IAttackTypeMemberState extends IMemberState {

    AttackTypes getType();
}
