package de.pnp.manager.ui.battle.state;

import de.pnp.manager.model.character.PnPCharacter;
import de.pnp.manager.model.character.data.AttackTypes;
import de.pnp.manager.model.character.state.interfaces.IMemberStateImpl;

public interface MemberStateProducer {

    IMemberStateImpl create(String name, int duration, boolean activeRounder, float power, boolean isRandom, AttackTypes type, PnPCharacter source);
}
