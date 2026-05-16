package de.pnp.manager.component.upgrade.effect;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.base.MoreObjects;

/**
 * A simple {@link ItemEffect} which only has a description.
 */
public class SimpleItemEffect extends ItemEffect {

    @JsonCreator
    public SimpleItemEffect(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("description", description)
                .toString();
    }
}
