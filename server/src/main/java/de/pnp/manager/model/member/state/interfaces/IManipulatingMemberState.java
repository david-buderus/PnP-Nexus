package de.pnp.manager.model.member.state.interfaces;

import de.pnp.manager.model.member.interfaces.IBattleMember;

public interface IManipulatingMemberState extends IMemberState {

    void apply(IBattleMember member);
}
