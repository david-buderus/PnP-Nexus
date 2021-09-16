package de.pnp.manager.model.character.state;

public interface IPowerMemberState extends IMemberState {

    float getMaxPower();

    float getCurrentPower();

    void setCurrentPower(float currentPower);
}
