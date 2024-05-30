package de.pnp.manager.component.upgrade.effect;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.base.MoreObjects;

/**
 * A simple {@link UpgradeEffect} which only has a description.
 */
public class SimpleUpgradeEffect extends UpgradeEffect {

    @JsonCreator
    public SimpleUpgradeEffect(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("description", description)
            .toString();
    }
}
