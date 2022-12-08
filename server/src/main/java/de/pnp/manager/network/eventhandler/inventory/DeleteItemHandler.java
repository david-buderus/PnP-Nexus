package de.pnp.manager.network.eventhandler.inventory;

import de.pnp.manager.model.character.IInventory;
import de.pnp.manager.model.manager.Manager;
import de.pnp.manager.network.interfaces.Client;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.message.error.DeniedMessage;
import de.pnp.manager.network.message.error.NotPossibleMessage;
import de.pnp.manager.network.message.inventory.DeleteItemRequestMessage;
import de.pnp.manager.network.message.inventory.InventoryUpdateNotificationMessage;
import de.pnp.manager.network.state.INonConditionalEventHandler;

import java.util.Calendar;
import java.util.Collections;

import static de.pnp.manager.main.LanguageUtility.getMessage;

public class DeleteItemHandler implements INonConditionalEventHandler<BaseMessage> {

    protected Client client;
    protected Manager manager;
    protected Calendar calendar;

    public DeleteItemHandler(Client client, Calendar calendar, Manager manager) {
        this.client = client;
        this.manager = manager;
        this.calendar = calendar;
    }

    @Override
    public void applyNonConditionalEvent(BaseMessage message) {
        if (message instanceof DeleteItemRequestMessage) {
            DeleteItemRequestMessage.DeleteItemData data = ((DeleteItemRequestMessage) message).getData();

            if (client.hasAccessToInventory(data.getInventoryID())) {
                IInventory inventory = manager.getInventoryHandler().getInventory(client.getSessionID(), data.getInventoryID());

                if (inventory != null && inventory.removeAll(data.getItems())) {
                    manager.getNetworkHandler().broadcast(new InventoryUpdateNotificationMessage(
                            data.getInventoryID(),
                            Collections.emptyList(),
                            data.getItems(),
                            calendar.getTime()
                    ), manager.getNetworkHandler().getClientsWithInventoryAccess(data.getInventoryID()));
                } else {
                    client.sendMessage(new NotPossibleMessage(getMessage("message.items.amount.notEnough"), calendar.getTime()));
                }

            } else {
                client.sendMessage(new DeniedMessage(getMessage("message.error.denied.inventory"), calendar.getTime()));
            }
        }
    }
}
