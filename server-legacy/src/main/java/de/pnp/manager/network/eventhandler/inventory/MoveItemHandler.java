package de.pnp.manager.network.eventhandler.inventory;

import de.pnp.manager.model.character.IInventory;
import de.pnp.manager.model.manager.Manager;
import de.pnp.manager.network.interfaces.Client;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.message.error.DeniedMessage;
import de.pnp.manager.network.message.error.NotPossibleMessage;
import de.pnp.manager.network.message.inventory.InventoryUpdateNotificationMessage;
import de.pnp.manager.network.message.inventory.MoveItemRequestMessage;
import de.pnp.manager.network.state.INonConditionalEventHandler;

import java.util.Calendar;
import java.util.Collections;

import static de.pnp.manager.main.LanguageUtility.getMessage;

public class MoveItemHandler implements INonConditionalEventHandler<BaseMessage> {

    protected Client client;
    protected Calendar calendar;
    protected Manager manager;

    public MoveItemHandler(Client client, Calendar calendar, Manager manager) {
        this.client = client;
        this.calendar = calendar;
        this.manager = manager;
    }

    @Override
    public void applyNonConditionalEvent(BaseMessage message) {
        if (message instanceof MoveItemRequestMessage) {
            MoveItemRequestMessage.MoveItemData data = ((MoveItemRequestMessage) message).getData();

            if (client.hasAccessToInventory(data.getFrom())) {
                IInventory from = manager.getInventoryHandler().getInventory(client.getSessionID(), data.getFrom());

                if (from != null && from.containsAmount(data.getItems())) {

                    IInventory to = manager.getInventoryHandler().getInventory(client.getSessionID(), data.getTo());

                    if (to != null && to.addAll(data.getItems())) {
                        from.removeAll(data.getItems());

                        manager.getNetworkHandler().broadcast(new InventoryUpdateNotificationMessage(
                                data.getFrom(),
                                Collections.emptyList(),
                                data.getItems(),
                                calendar.getTime()
                        ), manager.getNetworkHandler().getClientsWithInventoryAccess(data.getFrom()));
                        manager.getNetworkHandler().broadcast(new InventoryUpdateNotificationMessage(
                                data.getTo(),
                                data.getItems(),
                                Collections.emptyList(),
                                calendar.getTime()
                        ), manager.getNetworkHandler().getClientsWithInventoryAccess(data.getTo()));

                    } else {
                        client.sendMessage(new NotPossibleMessage(getMessage("message.items.space.notEnough"), calendar.getTime()));
                    }

                } else {
                    client.sendMessage(new NotPossibleMessage(getMessage("message.items.amount.notEnough"), calendar.getTime()));
                }

            } else {
                client.sendMessage(new DeniedMessage(getMessage("message.error.denied.inventory"), calendar.getTime()));
            }
        }
    }
}
