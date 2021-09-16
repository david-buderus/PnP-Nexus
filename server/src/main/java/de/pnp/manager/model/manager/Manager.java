package de.pnp.manager.model.manager;

import de.pnp.manager.network.ServerNetworkHandler;
import de.pnp.manager.network.interfaces.NetworkHandler;

public class Manager {

    protected BattleHandler battleHandler;
    protected ServerNetworkHandler networkHandler;
    protected CharacterHandler characterHandler;

    public Manager() {
        this.battleHandler = new BattleHandler(this);
        this.networkHandler = new ServerNetworkHandler(this);
        this.characterHandler = new CharacterHandler();

        this.networkHandler.start();
    }

    public BattleHandler getBattleHandler() {
        return battleHandler;
    }

    public NetworkHandler getNetworkHandler() {
        return networkHandler;
    }

    public CharacterHandler getCharacterHandler() {
        return characterHandler;
    }
}
