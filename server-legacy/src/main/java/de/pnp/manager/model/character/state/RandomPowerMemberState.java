package de.pnp.manager.model.character.state;

import de.pnp.manager.model.character.PnPCharacter;
import de.pnp.manager.model.character.state.interfaces.IRandomPowerMemberStateImpl;

import java.util.Random;

public abstract class RandomPowerMemberState extends PowerMemberState implements IRandomPowerMemberStateImpl {

    protected static final Random RAND = new Random();

    protected boolean isRandom;

    public RandomPowerMemberState(String name, int imageID, int duration, boolean activeRounder, PnPCharacter source, float maxPower, boolean isRandom) {
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
