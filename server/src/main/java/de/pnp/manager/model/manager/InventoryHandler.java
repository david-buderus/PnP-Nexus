package de.pnp.manager.model.manager;

import de.pnp.manager.model.character.Inventory;
import de.pnp.manager.model.character.PnPCharacter;
import de.pnp.manager.model.other.Container;
import de.pnp.manager.network.message.character.DismissCharactersMessage;
import de.pnp.manager.network.message.inventory.DismissInventoriesMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.rmi.server.UID;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class InventoryHandler {

    private static short ID_COUNTER = 0;

    protected static synchronized String getNextInventoryID(){
        return "I-" + new UID(++ID_COUNTER);
    }

    protected Manager manager;
    protected Map<String, ObservableList<Container>> sessionContainerMap;

    public InventoryHandler(Manager manager) {
        this.manager = manager;
        this.sessionContainerMap = new HashMap<>();
    }

    public Container createContainer(String sessionID, String name, Inventory inventory) {
        Container container = new Container(getNextInventoryID(), name, inventory);
        this.getContainers(sessionID).add(container);

        return container;
    }

    public void deleteContainer(String sessionID, String inventoryID) {
        Collection<Container> containers = this.sessionContainerMap.get(sessionID);

        if (containers != null) {
            containers.removeIf(c -> c.getInventoryID().equals(inventoryID));
            manager.getNetworkHandler().activeBroadcast(
                    new DismissInventoriesMessage(inventoryID, Calendar.getInstance().getTime()),
                    manager.getNetworkHandler().clientsProperty().filtered(c -> c.hasAccessToInventory(inventoryID))
            );
        }
    }

    public ObservableList<Container> getContainers(String sessionID) {
        return sessionContainerMap.computeIfAbsent(sessionID, id -> createObservableList());
    }

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

        PnPCharacter character = manager.getCharacterHandler().getCharacter(sessionID, id);

        return character != null ? (Inventory) character.getInventory() : null;
    }

    private ObservableList<Container> createObservableList() {
        return FXCollections.observableArrayList();
    }
}
