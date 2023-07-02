package de.pnp.manager.component.upgrade.effect;

import java.util.Objects;

/**
 * An {@link UpgradeEffect} which multiplies an attribute by its factor.
 */
public class MultiplicativeUpgradeEffect extends UpgradeEffect {

    private final float factor;

    public MultiplicativeUpgradeEffect(String description, EUpgradeManipulator upgradeManipulator, float factor) {
        super(description, upgradeManipulator);
        this.factor = factor;
    }

    @Override
    protected float apply(float value) {
        return value * this.factor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        MultiplicativeUpgradeEffect that = (MultiplicativeUpgradeEffect) o;
        return Float.compare(that.factor, factor) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), factor);
    }
}
