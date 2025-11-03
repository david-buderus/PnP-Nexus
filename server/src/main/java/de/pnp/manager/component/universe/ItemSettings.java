package de.pnp.manager.component.universe;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Settings for items
 */
public class ItemSettings extends SettingsBase {

    /**
     * The default settings
     */
    public static final ItemSettings DEFAULT = new ItemSettings(-1, false, false);

    @NotNull
    private final int wearFactor;

    @NotNull
    private final boolean usingProtection;

    @NotNull
    private final boolean shieldUsingDice;

    @JsonCreator
    public ItemSettings(int wearFactor, boolean usingProtection, boolean shieldUsingDice) {
        this.wearFactor = wearFactor;
        this.usingProtection = usingProtection;
        this.shieldUsingDice = shieldUsingDice;
    }

    public int getWearFactor() {
        return wearFactor;
    }

    public boolean isUsingProtection() {
        return usingProtection;
    }

    public boolean isShieldUsingDice() {
        return shieldUsingDice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ItemSettings that = (ItemSettings) o;
        return getWearFactor() == that.getWearFactor() && isUsingProtection() == that.isUsingProtection()
            && isShieldUsingDice() == that.isShieldUsingDice();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getWearFactor(), isUsingProtection(), isShieldUsingDice());
    }
}
