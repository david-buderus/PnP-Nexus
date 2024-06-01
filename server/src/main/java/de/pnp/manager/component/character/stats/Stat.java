package de.pnp.manager.component.character.stats;

import jakarta.validation.constraints.PositiveOrZero;

/**
 * A single stat of a character.
 */
public class Stat {

    @PositiveOrZero
    private final int rawValue;
    
    private int flatModifier;

    public Stat(int rawValue) {
        this(rawValue, 0);
    }

    public Stat(int rawValue, int flatModifier) {
        this.rawValue = rawValue;
        this.flatModifier = flatModifier;
    }

    public int getRawValue() {
        return rawValue;
    }

    public int getFlatModifier() {
        return flatModifier;
    }

    public int getValue() {
        return getRawValue() + getFlatModifier();
    }

    public void setFlatModifier(int flatModifier) {
        this.flatModifier = flatModifier;
    }
}
