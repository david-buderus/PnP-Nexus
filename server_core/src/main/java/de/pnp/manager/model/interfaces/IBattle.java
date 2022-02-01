package de.pnp.manager.model.interfaces;

import de.pnp.manager.model.character.IPnPCharacter;

public interface IBattle {
    void addToHealStatistic(IPnPCharacter source, int amount);

    void addToDamageStatistic(IPnPCharacter source, int damage);
}
