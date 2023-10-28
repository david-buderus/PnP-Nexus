package de.pnp.manager.component.upgrade.effect;

/**
 * A simple {@link UpgradeEffect} which only has a description.
 */
public class SimpleUpgradeEffect extends UpgradeEffect {

    /**
     * Returns a new {@link SimpleUpgradeEffect} with the given description and the
     * {@link EUpgradeManipulator}.{@link EUpgradeManipulator#NONE NONE}.
     */
    public static SimpleUpgradeEffect create(String description) {
        return new SimpleUpgradeEffect(description, EUpgradeManipulator.NONE);
    }

    public SimpleUpgradeEffect(String description, EUpgradeManipulator upgradeManipulator) {
        super(description, upgradeManipulator);
    }

    @Override
    protected float apply(float value) {
        return value;
    }
}
