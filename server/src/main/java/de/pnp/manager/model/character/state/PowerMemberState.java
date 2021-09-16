package de.pnp.manager.model.character.state;

import de.pnp.manager.model.character.PnPCharacter;
import de.pnp.manager.model.character.state.interfaces.IPowerMemberStateImpl;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;

public abstract class PowerMemberState extends ActiveRounderMemberState implements IPowerMemberStateImpl {

    protected final float maxPower;
    protected final FloatProperty currentPower;

    public PowerMemberState(String name, int imageID, int duration, boolean activeRounder, PnPCharacter source, float maxPower) {
        super(name, imageID, duration, activeRounder, source);
        this.maxPower = maxPower;
        this.currentPower = new SimpleFloatProperty(maxPower);
    }

    @Override
    public FloatProperty currentPowerProperty() {
        return currentPower;
    }

    @Override
    public float getMaxPower() {
        return maxPower;
    }
}
