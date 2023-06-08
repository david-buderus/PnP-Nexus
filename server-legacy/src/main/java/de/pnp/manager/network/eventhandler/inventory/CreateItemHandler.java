package de.pnp.manager.network.eventhandler.inventory;

import de.pnp.manager.model.character.IInventory;
import de.pnp.manager.model.manager.Manager;
import de.pnp.manager.network.interfaces.Client;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.message.error.DeniedMessage;
import de.pnp.manager.network.message.error.NotPossibleMessage;
import de.pnp.manager.network.message.inventory.CreateItemRequestMessage;
import de.pnp.manager.network.message.inventory.InventoryUpdateNotificationMessage;
import de.pnp.manager.network.state.INonConditionalEventHandler;

import java.util.Calendar;
import java.util.Collections;

import static de.pnp.manager.main.LanguageUtility.getMessage;

public class CreateItemHandler implements INonConditionalEventHandler<BaseMessage> {

    protected Client client;
    protected Calendar calendar;
    protected Manager manager;

    public CreateItemHandler(Client client, Calendar calendar, Manager manager) {
        this.client = client;
        this.calendar = calendar;
        this.manager = manager;
    }

    @Override
    public void applyNonConditionalEvent(BaseMessage message) {
        if (message instanceof CreateItemRequestMessage) {
            CreateItemRequestMessage.CreateItemData data = ((CreateItemRequestMessage) message).getData();

            if (client.hasAccessToInventory(data.getInventoryID())) {
                IInventory inventory = manager.getInventoryHandler().getInventory(client.getSessionID(), data.getInventoryID());

                if (inventory != null && inventory.addAll(data.getItems())) {
                    manager.getNetworkHandler().broadcast(new InventoryUpdateNotificationMessage(
                            data.getInventoryID(),
                            data.getItems(),
                            Collections.emptyList(),
                            calendar.getTime()
                    ), manager.getNetworkHandler().getClientsWithInventoryAccess(data.getInventoryID()));
                } else {
                    client.sendMessage(new NotPossibleMessage(getMessage("message.items.space.notEnough"), calendar.getTime()));
                }

            } else {
                client.sendMessage(new DeniedMessage(getMessage("message.error.denied.inventory"), calendar.getTime()));
            }
        }
    }
}
