package de.pnp.manager.network.eventhandler.currency;

import de.pnp.manager.model.ICurrency;
import de.pnp.manager.model.character.IInventory;
import de.pnp.manager.model.character.PlayerCharacter;
import de.pnp.manager.model.character.PnPCharacter;
import de.pnp.manager.model.item.IItem;
import de.pnp.manager.model.manager.Manager;
import de.pnp.manager.network.interfaces.Client;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.message.character.currency.CurrencyData;
import de.pnp.manager.network.message.character.currency.CurrencyUpdateNotificationMessage;
import de.pnp.manager.network.message.character.currency.MoveCurrencyData;
import de.pnp.manager.network.message.character.currency.MoveCurrencyToInventoryRequestMessage;
import de.pnp.manager.network.message.error.DeniedMessage;
import de.pnp.manager.network.message.error.NotPossibleMessage;
import de.pnp.manager.network.message.inventory.InventoryUpdateNotificationMessage;
import de.pnp.manager.network.state.INonConditionalEventHandler;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;

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

                    if (newCurrencyFrom.getCoinValue() >= 0) {

                        Collection<IItem> items = data.getCurrency().toItems();

                        if (to != null && to.addAll(items)) {
                            fromPlayer.setCurrency(newCurrencyFrom);

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
