package de.pnp.manager.network.eventhandler.currency;

import de.pnp.manager.model.Currency;
import de.pnp.manager.model.ICurrency;
import de.pnp.manager.model.character.Inventory;
import de.pnp.manager.model.character.PlayerCharacter;
import de.pnp.manager.model.character.PnPCharacter;
import de.pnp.manager.model.item.IItem;
import de.pnp.manager.model.manager.Manager;
import de.pnp.manager.network.interfaces.Client;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.message.character.currency.CurrencyData;
import de.pnp.manager.network.message.character.currency.CurrencyUpdateNotificationMessage;
import de.pnp.manager.network.message.character.currency.MoveCurrencyFromInventoryRequestMessage;
import de.pnp.manager.network.message.error.DeniedMessage;
import de.pnp.manager.network.message.error.NotPossibleMessage;
import de.pnp.manager.network.message.inventory.InventoryUpdateNotificationMessage;
import de.pnp.manager.network.state.INonConditionalEventHandler;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;

import static de.pnp.manager.main.LanguageUtility.getMessage;

public class MoveCurrencyFromInventoryHandler implements INonConditionalEventHandler<BaseMessage> {

    protected Client client;
    protected Calendar calendar;
    protected Manager manager;

    public MoveCurrencyFromInventoryHandler(Client client, Calendar calendar, Manager manager) {
        this.client = client;
        this.calendar = calendar;
        this.manager = manager;
    }

    @Override
    public void applyNonConditionalEvent(BaseMessage message) {
        if (message instanceof MoveCurrencyFromInventoryRequestMessage) {
            MoveCurrencyFromInventoryRequestMessage.MoveCurrencyFromInventoryData data = ((MoveCurrencyFromInventoryRequestMessage) message).getData();

            Collection<String> coinNames = Arrays.asList(getMessage("coin.copper"), getMessage("coin.silver"), getMessage("coin.gold"));

            if (data.getCurrencyItems().stream().allMatch(item -> coinNames.contains(item.getName()))) {
                if (client.hasAccessToInventory(data.getFrom())) {
                    Inventory from = manager.getInventoryHandler().getInventory(client.getSessionID(), data.getFrom());

                    if (from != null && from.containsAmount(data.getCurrencyItems())) {

                        PnPCharacter to = manager.getCharacterHandler().getCharacter(client.getSessionID(), data.getTo());

                        if (to instanceof PlayerCharacter) {
                            PlayerCharacter toPlayer = (PlayerCharacter) to;

                            from.removeAll(data.getCurrencyItems());

                            ICurrency currency = data.getCurrencyItems().stream()
                                    .map(IItem::getCurrencyWithAmount).reduce(new Currency(0), ICurrency::add);

                            ICurrency newCurrency = toPlayer.getCurrency().add(currency);
                            toPlayer.setCurrency(newCurrency);

                            manager.getNetworkHandler().broadcast(new CurrencyUpdateNotificationMessage(
                                    new CurrencyData(data.getTo(), newCurrency),
                                    calendar.getTime()
                            ), manager.getNetworkHandler().getClientsWithCharacterControl(data.getTo()));

                            manager.getNetworkHandler().broadcast(new InventoryUpdateNotificationMessage(
                                    data.getFrom(),
                                    Collections.emptyList(),
                                    data.getCurrencyItems(),
                                    calendar.getTime()
                            ), manager.getNetworkHandler().getClientsWithInventoryAccess(data.getFrom()));

                        } else {
                            client.sendMessage(new NotPossibleMessage(getMessage("message.currency.notPossible.notPlayer"), calendar.getTime()));
                        }
                    } else {
                        client.sendMessage(new NotPossibleMessage(getMessage("message.items.amount.notEnough"), calendar.getTime()));
                    }
                } else {
                    client.sendMessage(new DeniedMessage(getMessage("message.error.denied.inventory"), calendar.getTime()));
                }
            } else {
                client.sendMessage(new DeniedMessage(getMessage("message.error.notCurrency"), calendar.getTime()));
            }
        }
    }
}
