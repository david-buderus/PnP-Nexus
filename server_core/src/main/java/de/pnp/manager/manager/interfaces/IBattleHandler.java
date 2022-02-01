package de.pnp.manager.manager.interfaces;

import de.pnp.manager.model.interfaces.IBattle;

public interface IBattleHandler<BattleImpl extends IBattle> {

    BattleImpl createBattle();

    BattleImpl createBattle(String sessionID);

    void deleteBattle(String sessionID, String battleID);

    void deleteBattle(String battleID);
}
