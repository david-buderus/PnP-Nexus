package de.pnp.manager.model.manager;

import de.pnp.manager.network.ServerNetworkHandler;
import de.pnp.manager.network.interfaces.NetworkHandler;

import java.io.Closeable;
import java.io.IOException;

public class Manager implements Closeable {

    protected BattleHandler battleHandler;
    protected ServerNetworkHandler networkHandler;
    protected CharacterHandler characterHandler;
    protected InventoryHandler inventoryHandler;

    public Manager() {
        this.battleHandler = new BattleHandler(this);
        this.networkHandler = new ServerNetworkHandler(this);
        this.characterHandler = new CharacterHandler(this);
        this.inventoryHandler = new InventoryHandler(this);

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

    public InventoryHandler getInventoryHandler() {
        return inventoryHandler;
    }

    @Override
    public void close() throws IOException {
        this.networkHandler.close();
    }
}
