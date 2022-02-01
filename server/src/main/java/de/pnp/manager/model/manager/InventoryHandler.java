package de.pnp.manager.model.manager;

import de.pnp.manager.main.LanguageUtility;
import de.pnp.manager.manager.handlers.AbstractInventoryHandler;
import de.pnp.manager.model.character.Inventory;
import de.pnp.manager.model.other.Container;
import de.pnp.manager.network.message.inventory.RevokeInventoriesMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class InventoryHandler extends AbstractInventoryHandler {

    private static short ID_COUNTER = 0;

    protected static synchronized String getNextInventoryID(){
        return String.format(LanguageUtility.getCurrentLanguage().getLocale(), "I-%05d", ++ID_COUNTER);
    }

    protected Map<String, ObservableList<Container>> sessionContainerMap;

    public InventoryHandler(Manager manager) {
        super(manager);
        this.sessionContainerMap = new HashMap<>();
    }

    @Override
    public Container createContainer(String sessionID, String name, Inventory inventory) {
        Container container = new Container(getNextInventoryID(), name, inventory);
        this.getContainers(sessionID).add(container);

        return container;
    }

    @Override
    public void deleteContainer(String sessionID, String inventoryID) {
        Collection<Container> containers = this.sessionContainerMap.get(sessionID);

        if (containers != null) {
            containers.removeIf(c -> c.getInventoryID().equals(inventoryID));
            manager.getNetworkHandler().activeBroadcast(
                    new RevokeInventoriesMessage(inventoryID, Calendar.getInstance().getTime()),
                    manager.getNetworkHandler().getClients().stream().filter(c -> c.hasAccessToInventory(inventoryID))
                            .collect(Collectors.toList())
            );
        }
    }

    @Override
    public ObservableList<Container> getContainers(String sessionID) {
        return sessionContainerMap.computeIfAbsent(sessionID, id -> createObservableList());
    }

    private ObservableList<Container> createObservableList() {
        return FXCollections.observableArrayList();
    }
}
