package model.member.state.interfaces;

import model.member.BattleMember;
import model.member.interfaces.IBattleMember;

public interface INumberMemberState<N extends Number> extends IMemberState {

    N apply(IBattleMember member, N input);
}
