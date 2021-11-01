package de.pnp.manager.model.manager;

import de.pnp.manager.model.battle.Battle;
import de.pnp.manager.model.character.IPnPCharacter;
import de.pnp.manager.model.character.PnPCharacter;
import de.pnp.manager.model.character.PnPCharacterFactory;

public interface PnPCharacterProducer<C extends IPnPCharacter> {

    PnPCharacterProducer<PnPCharacter> DEFAULT = PnPCharacterFactory::createDefaultCharacter;

    C create(String characterID, Battle battle);
}
