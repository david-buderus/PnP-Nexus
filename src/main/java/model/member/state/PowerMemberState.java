package model.member.state;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import model.member.BattleMember;
import model.member.state.interfaces.IPowerMemberState;

public abstract class PowerMemberState extends ActiveRounderMemberState implements IPowerMemberState {

    protected final float maxPower;
    protected final FloatProperty currentPower;

    public PowerMemberState(String name, int imageID, int duration, boolean activeRounder, BattleMember source, float maxPower) {
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
