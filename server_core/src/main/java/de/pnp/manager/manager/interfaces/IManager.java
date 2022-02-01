package de.pnp.manager.manager.interfaces;

import de.pnp.manager.manager.handlers.AbstractInventoryHandler;
import de.pnp.manager.model.character.IPnPCharacter;
import de.pnp.manager.model.interfaces.IBattle;
import de.pnp.manager.network.interfaces.INetworkHandler;

public interface IManager<BattleImpl extends IBattle, PnPCharacterImpl extends IPnPCharacter> {

    IBattleHandler<BattleImpl> getBattleHandler();

    INetworkHandler getNetworkHandler();

    ICharacterHandler<PnPCharacterImpl> getCharacterHandler();

    AbstractInventoryHandler getInventoryHandler();
}
