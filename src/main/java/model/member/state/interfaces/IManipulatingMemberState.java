package model.member.state.interfaces;

import model.member.BattleMember;

public interface IManipulatingMemberState extends IMemberState {

    void apply(BattleMember member);
}
