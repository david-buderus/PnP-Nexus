package de.pnp.manager.model.character.state;

import de.pnp.manager.model.character.data.IAttackTypes;

public interface IAttackTypeMemberState extends IMemberState {

    IAttackTypes getType();
}
