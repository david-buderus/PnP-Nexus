package de.pnp.manager.component.upgrade.effect;

import java.util.Objects;

/**
 * An {@link IUpgradeEffect} which is only represented by its description.
 */
public class SimpleUpgradeEffect implements IUpgradeEffect {

    private final String description;

    public SimpleUpgradeEffect(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimpleUpgradeEffect that = (SimpleUpgradeEffect) o;
        return Objects.equals(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDescription());
    }
}
