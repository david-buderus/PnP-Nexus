package de.pnp.manager.model.character.state;

import de.pnp.manager.model.character.IPnPCharacter;

public interface IManipulatingMemberState extends IMemberState {

    void apply(IPnPCharacter character);
}
