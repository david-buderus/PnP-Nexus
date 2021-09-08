package model.member.state.interfaces;

import model.member.interfaces.IBattleMember;

public interface IManipulatingMemberState extends IMemberState {

    void apply(IBattleMember member);
}
