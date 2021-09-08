package model.member.state.interfaces;

import java.util.Random;

public interface IRandomMemberState extends IMemberState {

    boolean isRandom();

    Random getRandom();
}
