package de.pnp.manager.manager.interfaces;

import de.pnp.manager.model.character.IPnPCharacter;
import de.pnp.manager.model.interfaces.IBattle;

public interface PnPCharacterProducer<C extends IPnPCharacter> {
    C create(String characterID, IBattle battle);
}
