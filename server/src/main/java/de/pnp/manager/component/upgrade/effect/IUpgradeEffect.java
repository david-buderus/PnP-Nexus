package de.pnp.manager.component.upgrade.effect;

import de.pnp.manager.component.upgrade.Upgrade;

/**
 * Represents an effect of an {@link Upgrade}.
 */
public interface IUpgradeEffect {

    /**
     * A human-readable description of the effect.
     */
    String getDescription();
}
