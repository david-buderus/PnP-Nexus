package de.pnp.manager.component.character.traits;

import de.pnp.manager.component.attributes.IAttribute;

/**
 * Modifies the stat of a {@link Character}.
 */
public class StatModifyingTrait implements ICharacterTrait {

    private final int modifier;
    private final IAttribute attribute;


    public StatModifyingTrait(int modifier, IAttribute attribute) {
        this.modifier = modifier;
        this.attribute = attribute;
    }

    public int getModifier() {
        return modifier;
    }

    public IAttribute getAttribute() {
        return attribute;
    }
}
