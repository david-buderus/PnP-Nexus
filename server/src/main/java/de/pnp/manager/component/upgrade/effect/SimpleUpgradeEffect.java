package de.pnp.manager.component.upgrade.effect;

/**
 * A simple {@link UpgradeEffect} which only has a description.
 */
public class SimpleUpgradeEffect extends UpgradeEffect {

    public static SimpleUpgradeEffect create(String description) {
        return new SimpleUpgradeEffect(description, EUpgradeManipulator.NONE);
    }

    private SimpleUpgradeEffect(String description, EUpgradeManipulator upgradeManipulator) {
        super(description, upgradeManipulator);
    }

    @Override
    protected float apply(float value) {
        return value;
    }
}
