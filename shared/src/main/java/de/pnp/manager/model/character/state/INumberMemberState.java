package de.pnp.manager.model.character.state;

import de.pnp.manager.model.character.IPnPCharacter;

public interface INumberMemberState<N extends Number> extends IMemberState {

    N apply(IPnPCharacter character, N input);
}
