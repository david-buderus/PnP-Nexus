package de.pnp.manager.model.manager;

import de.pnp.manager.network.ServerNetworkHandler;
import de.pnp.manager.network.interfaces.NetworkHandler;

import java.io.Closeable;
import java.io.IOException;

public class Manager implements Closeable {

    protected BattleHandler battleHandler;
    protected ServerNetworkHandler networkHandler;
    protected CharacterHandler characterHandler;

    public Manager() {
        this.battleHandler = new BattleHandler(this);
        this.networkHandler = new ServerNetworkHandler(this);
        this.characterHandler = new CharacterHandler(this);

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

    @Override
    public void close() throws IOException {
        this.networkHandler.close();
    }
}
