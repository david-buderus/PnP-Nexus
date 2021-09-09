package de.pnp.manager.model.member.state;

import de.pnp.manager.model.member.interfaces.IBattleMember;
import de.pnp.manager.model.member.state.interfaces.IRandomPowerMemberState;

import java.util.Random;

public abstract class RandomPowerMemberState extends PowerMemberState implements IRandomPowerMemberState {

    protected static final Random RAND = new Random();

    protected boolean isRandom;

    public RandomPowerMemberState(String name, int imageID, int duration, boolean activeRounder, IBattleMember source, float maxPower, boolean isRandom) {
        super(name, imageID, duration, activeRounder, source, maxPower);
        this.isRandom = isRandom;
    }

    @Override
    public boolean isRandom() {
        return isRandom;
    }

    @Override
    public Random getRandom() {
        return RAND;
    }
}
