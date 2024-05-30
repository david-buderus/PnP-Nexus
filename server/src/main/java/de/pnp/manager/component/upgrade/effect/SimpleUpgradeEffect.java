package de.pnp.manager.component.upgrade.effect;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * A simple {@link UpgradeEffect} which only has a description.
 */
public class SimpleUpgradeEffect extends UpgradeEffect {

    @JsonCreator
    public SimpleUpgradeEffect(String description) {
        super(description);
    }
}
