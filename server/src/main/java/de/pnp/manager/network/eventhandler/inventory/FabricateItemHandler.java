package de.pnp.manager.network.eventhandler.inventory;

import de.pnp.manager.main.Database;
import de.pnp.manager.model.IFabrication;
import de.pnp.manager.model.character.IInventory;
import de.pnp.manager.model.item.IItem;
import de.pnp.manager.model.item.Item;
import de.pnp.manager.model.manager.Manager;
import de.pnp.manager.network.interfaces.Client;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.message.error.DeniedMessage;
import de.pnp.manager.network.message.error.NotPossibleMessage;
import de.pnp.manager.network.message.inventory.FabricateItemRequestMessage;
import de.pnp.manager.network.message.inventory.InventoryUpdateNotificationMessage;
import de.pnp.manager.network.state.INonConditionalEventHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import static de.pnp.manager.main.LanguageUtility.getMessage;

public class FabricateItemHandler implements INonConditionalEventHandler<BaseMessage> {

    protected Client client;
    protected Calendar calendar;
    protected Manager manager;

    public FabricateItemHandler(Client client, Calendar calendar, Manager manager) {
        this.client = client;
        this.calendar = calendar;
        this.manager = manager;
    }

    @Override
    public void applyNonConditionalEvent(BaseMessage message) {
        if (message instanceof FabricateItemRequestMessage) {
            FabricateItemRequestMessage.FabricateItemData data = ((FabricateItemRequestMessage) message).getData();
            IFabrication fabrication = data.getFabrication();


            if (Database.fabricationList.contains(data.getFabrication())) {
                if (client.hasAccessToInventory(data.getInventoryID())) {
                    IInventory inventory = manager.getInventoryHandler().getInventory(client.getSessionID(), data.getInventoryID());

                    if (inventory != null && inventory.containsAmount(fabrication.getMaterials())) {
                        inventory.removeAll(fabrication.getMaterials());

                        Collection<IItem> toAdd = new ArrayList<>();

                        IItem product = fabrication.getProduct().copy();
                        product.setAmount(fabrication.getProductAmount());
                        toAdd.add(product);

                        if (fabrication.getSideProduct() != null
                                && fabrication.getSideProductAmount() > 0
                                && !Item.EMPTY_ITEM.equals(fabrication.getSideProduct())
                        ) {
                            IItem sideProduct = fabrication.getSideProduct().copy();
                            sideProduct.setAmount(fabrication.getSideProductAmount());
                            toAdd.add(sideProduct);
                        }

                        if (inventory.addAll(toAdd)) {

                            manager.getNetworkHandler().broadcast(new InventoryUpdateNotificationMessage(
                                    data.getInventoryID(),
                                    toAdd,
                                    fabrication.getMaterials(),
                                    calendar.getTime()
                            ), manager.getNetworkHandler().getClientsWithInventoryAccess(data.getInventoryID()));

                        } else {
                            //Recreate the previous state
                            inventory.addAll(fabrication.getMaterials());
                            client.sendMessage(new NotPossibleMessage(getMessage("message.items.space.notEnough"), calendar.getTime()));
                        }

                    } else {
                        client.sendMessage(new NotPossibleMessage(getMessage("message.items.amount.notEnough"), calendar.getTime()));
                    }

                } else {
                    client.sendMessage(new DeniedMessage(calendar.getTime()));
                }
            } else {
                client.sendMessage(new DeniedMessage(calendar.getTime()));
            }
        }
    }
}
