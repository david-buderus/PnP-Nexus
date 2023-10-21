package de.pnp.manager.component.character.stats;

import de.pnp.manager.component.attributes.IAttribute;

public abstract class Stat<A extends IAttribute> {

    /**
     * The {@link IAttribute} of this stat.
     */
    private final A attribute;

    /**
     * The value of the stat.
     */
    protected int value;

    public Stat(A attribute, int value) {
        this.attribute = attribute;
        this.value = value;
    }

    public A getAttribute() {
        return attribute;
    }

    public int getValue() {
        return value;
    }
}
