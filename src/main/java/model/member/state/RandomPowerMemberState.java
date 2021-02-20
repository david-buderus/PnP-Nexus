package model.member.state;

import model.member.BattleMember;
import model.member.state.interfaces.IRandomPowerMemberState;

import java.util.Random;

public abstract class RandomPowerMemberState extends PowerMemberState implements IRandomPowerMemberState {

    protected boolean random;

    public RandomPowerMemberState(String name, int imageID, int duration, boolean activeRounder, BattleMember source, float maxPower, boolean random) {
        super(name, imageID, duration, activeRounder, source, maxPower);
        this.random = random;
    }

    @Override
    public boolean isRandom() {
        return random;
    }

    @Override
    public Random getRandom() {
        return RAND;
    }
}
