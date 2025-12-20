package de.pnp.manager.component.character.stats;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A single stat of a character.
 */
public class Stat {

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

    public void setFlatModifier(int flatModifier) {
        this.flatModifier = flatModifier;
    }

    /**
     * @return the resulting value of this stat.
     */
    @JsonIgnore
    public int getValue() {
        return getRawValue() + getFlatModifier();
    }
}
