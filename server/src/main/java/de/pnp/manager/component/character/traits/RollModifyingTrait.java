package de.pnp.manager.component.character.traits;

import de.pnp.manager.component.character.Talent;

/**
 * Modifies a {@link Talent} roll of a {@link Character}
 */
public class RollModifyingTrait implements ICharacterTrait {

    private final Talent talent;
    private final int modifier;

    public RollModifyingTrait(Talent talent, int modifier) {
        this.talent = talent;
        this.modifier = modifier;
    }

    public Talent getTalent() {
        return talent;
    }

    public int getModifier() {
        return modifier;
    }
}
