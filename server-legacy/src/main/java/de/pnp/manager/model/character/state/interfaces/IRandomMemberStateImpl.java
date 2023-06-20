package de.pnp.manager.model.character.state.interfaces;

import de.pnp.manager.model.character.state.IRandomMemberState;

import java.util.Random;

public interface IRandomMemberStateImpl extends IRandomMemberState, IMemberStateImpl {

    Random getRandom();
}
