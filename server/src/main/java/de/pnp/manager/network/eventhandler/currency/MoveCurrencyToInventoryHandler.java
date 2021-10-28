package de.pnp.manager.network.eventhandler.currency;

import de.pnp.manager.main.LanguageUtility;
import de.pnp.manager.main.Utility;
import de.pnp.manager.model.Currency;
import de.pnp.manager.model.ICurrency;
import de.pnp.manager.model.character.IInventory;
import de.pnp.manager.model.character.Inventory;
import de.pnp.manager.model.character.PlayerCharacter;
import de.pnp.manager.model.character.PnPCharacter;
import de.pnp.manager.model.item.Item;
import de.pnp.manager.model.manager.Manager;
import de.pnp.manager.network.interfaces.Client;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.message.character.currency.*;
import de.pnp.manager.network.message.error.DeniedMessage;
import de.pnp.manager.network.message.error.NotPossibleMessage;
import de.pnp.manager.network.message.inventory.InventoryUpdateNotificationMessage;
import de.pnp.manager.network.state.INonConditionalEventHandler;

import java.util.*;

import static de.pnp.manager.main.LanguageUtility.getMessage;

public class MoveCurrencyToInventoryHandler implements INonConditionalEventHandler<BaseMessage> {

    protected Client client;
    protected Calendar calendar;
    protected Manager manager;

    public MoveCurrencyToInventoryHandler(Client client, Calendar calendar, Manager manager) {
        this.client = client;
        this.calendar = calendar;
        this.manager = manager;
    }

    @Override
    public void applyNonConditionalEvent(BaseMessage message) {
        if (message instanceof MoveCurrencyToInventoryRequestMessage) {
            MoveCurrencyData data = ((MoveCurrencyToInventoryRequestMessage) message).getData();

            if (client.getControlledCharacters().contains(data.getFrom())) {
                PnPCharacter from = manager.getCharacterHandler().getCharacter(client.getSessionID(), data.getFrom());

                if (from instanceof PlayerCharacter) {
                    PlayerCharacter fromPlayer = (PlayerCharacter) from;

                    IInventory to = manager.getInventoryHandler().getInventory(client.getSessionID(), data.getTo());

                    ICurrency newCurrencyFrom = fromPlayer.getCurrency().sub(data.getCurrency());

                    if (newCurrencyFrom.getCoinValue() > 0) {
                        fromPlayer.setCurrency(newCurrencyFrom);

                        int silverToCopper = Utility.getConfig().getInt("coin.silver.toCopper");
                        int goldToSilver = Utility.getConfig().getInt("coin.gold.toSilver");

                        int cost = data.getCurrency().getCoinValue();
                        int copper = cost % silverToCopper;
                        cost /= silverToCopper;
                        int silver = cost % goldToSilver;
                        cost /= goldToSilver;
                        int gold = cost;

                        Item copperItem = new Item(getMessage("coin.copper"));
                        copperItem.setAmount(copper);
                        copperItem.setCurrency(new Currency(1));
                        Item silverItem = new Item(getMessage("coin.silver"));
                        silverItem.setAmount(silver);
                        silverItem.setCurrency(new Currency(silverToCopper));
                        Item goldItem = new Item(getMessage("coin.gold"));
                        goldItem.setAmount(gold);
                        goldItem.setCurrency(new Currency(silverToCopper * goldToSilver));

                        Collection<Item> items = Arrays.asList(copperItem, silverItem, goldItem);

                        if (to != null && to.addAll(items)) {
                            manager.getNetworkHandler().broadcast(new CurrencyUpdateNotificationMessage(
                                    new CurrencyData(data.getFrom(), newCurrencyFrom),
                                    calendar.getTime()
                            ), manager.getNetworkHandler().getClientsWithCharacterControl(data.getFrom()));

                            manager.getNetworkHandler().broadcast(new InventoryUpdateNotificationMessage(
                                    data.getTo(),
                                    items,
                                    Collections.emptyList(),
                                    calendar.getTime()
                            ), manager.getNetworkHandler().getClientsWithInventoryAccess(data.getTo()));
                        } else {
                            client.sendMessage(new NotPossibleMessage(getMessage("message.items.space.notEnough"), calendar.getTime()));
                        }
                    } else {
                        client.sendMessage(new NotPossibleMessage(getMessage("message.currency.notPossible.notEnough"), calendar.getTime()));
                    }
                } else {
                    client.sendMessage(new NotPossibleMessage(getMessage("message.currency.notPossible.notPlayer"), calendar.getTime()));
                }
            } else {
                client.sendMessage(new DeniedMessage(getMessage("message.error.denied.character"), calendar.getTime()));
            }
        }
    }
}
