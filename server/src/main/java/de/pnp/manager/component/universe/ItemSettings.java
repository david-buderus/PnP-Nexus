package de.pnp.manager.component.universe;

import jakarta.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Settings for items
 */
public class ItemSettings extends SettingsBase {

    /**
     * The default settings
     */
    public static final ItemSettings DEFAULT = new ItemSettings(-1);

    @NotNull
    private final int wearFactor;

    public ItemSettings(int wearFactor) {
        this.wearFactor = wearFactor;
    }

    public int getWearFactor() {
        return wearFactor;
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
        return getWearFactor() == that.getWearFactor();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getWearFactor());
    }
}
