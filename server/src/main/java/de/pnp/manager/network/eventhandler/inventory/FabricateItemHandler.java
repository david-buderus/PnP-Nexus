package de.pnp.manager.network.eventhandler.inventory;

import de.pnp.manager.main.Database;
import de.pnp.manager.model.IFabrication;
import de.pnp.manager.model.IItemList;
import de.pnp.manager.model.ItemList;
import de.pnp.manager.model.character.IInventory;
import de.pnp.manager.model.character.Inventory;
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
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.*;
import java.util.stream.Collectors;

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
                if (client.hasAccessToInventory(data.getTo()) && data.getFrom().stream().allMatch(id -> client.hasAccessToInventory(id))) {

                    IInventory to = manager.getInventoryHandler().getInventory(client.getSessionID(), data.getTo());
                    Collection<ImmutablePair<String, IInventory>> from = data.getFrom().stream().map(id ->
                            new ImmutablePair<>(id, (IInventory) manager.getInventoryHandler().getInventory(client.getSessionID(), id))
                    ).collect(Collectors.toList());

                    IItemList missingItems = new ItemList(fabrication.getMaterials());
                    Map<String, IItemList> sourceTakeMap = new HashMap<>();

                    for (ImmutablePair<String, IInventory> sourcePair : from) {
                        if (missingItems.isEmpty()) {
                            break;
                        }

                        IInventory source = sourcePair.getRight();

                        IItemList diff = source.difference(missingItems);

                        IItemList takeFromThisSource = missingItems;
                        takeFromThisSource.removeAll(diff);

                        missingItems = diff;

                        sourceTakeMap.put(sourcePair.getLeft(), takeFromThisSource);
                    }

                    if (missingItems.isEmpty()) {

                        for (ImmutablePair<String, IInventory> sourcePair : from) {
                            sourcePair.getRight().removeAll(sourceTakeMap.get(sourcePair.getLeft()));
                        }

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

                        if (to.addAll(toAdd)) {

                            boolean updatedResultInventory = false;

                            for (ImmutablePair<String, IInventory> sourcePair : from) {
                                String fromID = sourcePair.getLeft();

                                if (fromID.equals(data.getTo())) {
                                    manager.getNetworkHandler().broadcast(new InventoryUpdateNotificationMessage(
                                            data.getTo(),
                                            toAdd,
                                            sourceTakeMap.get(fromID),
                                            calendar.getTime()
                                    ), manager.getNetworkHandler().getClientsWithInventoryAccess(fromID));
                                    updatedResultInventory = true;
                                } else {
                                    manager.getNetworkHandler().broadcast(new InventoryUpdateNotificationMessage(
                                            data.getTo(),
                                            Collections.emptyList(),
                                            sourceTakeMap.get(fromID),
                                            calendar.getTime()
                                    ), manager.getNetworkHandler().getClientsWithInventoryAccess(fromID));
                                }
                            }

                            if (!updatedResultInventory) {
                                manager.getNetworkHandler().broadcast(new InventoryUpdateNotificationMessage(
                                        data.getTo(),
                                        toAdd,
                                        Collections.emptyList(),
                                        calendar.getTime()
                                ), manager.getNetworkHandler().getClientsWithInventoryAccess(data.getTo()));
                            }

                        } else {
                            //Recreate the previous state
                            for (ImmutablePair<String, IInventory> sourcePair : from) {
                                sourcePair.getRight().addAll(sourceTakeMap.get(sourcePair.getLeft()));
                            }
                            client.sendMessage(new NotPossibleMessage(getMessage("message.items.space.notEnough"), calendar.getTime()));
                        }

                    } else {
                        client.sendMessage(new NotPossibleMessage(getMessage("message.items.amount.notEnough"), calendar.getTime()));
                    }
                } else {
                    client.sendMessage(new DeniedMessage(getMessage("message.error.denied.inventory"), calendar.getTime()));
                }
            } else {
                client.sendMessage(new DeniedMessage(getMessage("message.error.denied.fabrication"), calendar.getTime()));
            }
        }
    }
}
