package de.pnp.manager.component.upgrade.effect;

import java.util.Objects;

/**
 * An {@link UpgradeEffect} which multiplies an attribute.
 */
public class MultiplicativeUpgradeEffect extends UpgradeEffect {

    private final float value;

    public MultiplicativeUpgradeEffect(String description, EUpgradeManipulator upgradeManipulator, float value) {
        super(description, upgradeManipulator);
        this.value = value;
    }

    @Override
    protected float apply(float value) {
        return value * this.value;
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
        return Float.compare(that.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), value);
    }
}
