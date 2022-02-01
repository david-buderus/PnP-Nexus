package de.pnp.manager.manager.handlers;

import de.pnp.manager.manager.interfaces.IManager;
import de.pnp.manager.model.character.IPnPCharacter;
import de.pnp.manager.model.character.Inventory;
import de.pnp.manager.model.other.Container;

import java.util.Collection;

public abstract class AbstractInventoryHandler {

    protected IManager manager;

    public AbstractInventoryHandler(IManager manager) {
        this.manager = manager;
    }

    public abstract Container createContainer(String sessionID, String name, Inventory inventory);

    public abstract void deleteContainer(String sessionID, String inventoryID);

    public abstract Collection<Container> getContainers(String sessionID);

    public Container getContainer(String sessionID, String inventoryID) {
        return getContainers(sessionID).stream().filter(c -> c.getInventoryID().equals(inventoryID)).findFirst().orElse(null);
    }

    /**
     * Returns the inventory of a Container or a PnPCharacter
     *
     * @param sessionID in which the inventory exists
     * @param id id of a container or a character
     * @return the given inventory or null
     */
    public Inventory getInventory(String sessionID, String id) {
        Container container = getContainer(sessionID, id);

        if (container != null) {
            return container.getInventory();
        }

        IPnPCharacter character = manager.getCharacterHandler().getCharacter(sessionID, id);

        return character != null ? (Inventory) character.getInventory() : null;
    }
}
