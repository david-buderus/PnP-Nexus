package ui.battle.state;

import model.member.data.AttackTypes;
import model.member.interfaces.IBattleMember;
import model.member.state.interfaces.IMemberState;

public interface MemberStateProducer {

    IMemberState create(String name, int duration, boolean activeRounder, float power, boolean isRandom, AttackTypes type, IBattleMember source);
}
