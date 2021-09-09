package de.pnp.manager.model.member.state.interfaces;

import de.pnp.manager.model.member.interfaces.IBattleMember;

public interface INumberMemberState<N extends Number> extends IMemberState {

    N apply(IBattleMember member, N input);
}
