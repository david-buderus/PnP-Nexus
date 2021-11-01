package de.pnp.manager.model;

import de.pnp.manager.model.character.PnPCharacterInfo;

import java.util.Collection;

public interface IBattle {

    String getBattleID();

    String getName();

    int getCurrentRound();

    Collection<PnPCharacterInfo> getPlayers();

    Collection<PnPCharacterInfo> getEnemies();
}
