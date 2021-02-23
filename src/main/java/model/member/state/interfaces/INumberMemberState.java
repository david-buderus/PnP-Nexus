package model.member.state.interfaces;

import model.member.BattleMember;

public interface INumberMemberState<N extends Number> extends IMemberState {

    N apply(BattleMember member, N input);
}
