package de.pnp.manager.network.eventhandler.currency;

import de.pnp.manager.model.ICurrency;
import de.pnp.manager.model.character.PlayerCharacter;
import de.pnp.manager.model.character.PnPCharacter;
import de.pnp.manager.model.manager.Manager;
import de.pnp.manager.network.interfaces.Client;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.message.character.currency.CurrencyData;
import de.pnp.manager.network.message.character.currency.CurrencyUpdateNotificationMessage;
import de.pnp.manager.network.message.character.currency.DeleteCurrencyRequestMessage;
import de.pnp.manager.network.message.error.DeniedMessage;
import de.pnp.manager.network.message.error.NotPossibleMessage;
import de.pnp.manager.network.state.INonConditionalEventHandler;

import java.util.Calendar;

import static de.pnp.manager.main.LanguageUtility.getMessage;

public class DeleteCurrencyHandler implements INonConditionalEventHandler<BaseMessage> {

    protected Client client;
    protected Calendar calendar;
    protected Manager manager;

    public DeleteCurrencyHandler(Client client, Calendar calendar, Manager manager) {
        this.client = client;
        this.calendar = calendar;
        this.manager = manager;
    }

    @Override
    public void applyNonConditionalEvent(BaseMessage message) {
        if (message instanceof DeleteCurrencyRequestMessage) {
            CurrencyData data = ((DeleteCurrencyRequestMessage) message).getData();

            if (client.getControlledCharacters().contains(data.getCharacterID())) {
                PnPCharacter character = manager.getCharacterHandler().getCharacter(client.getSessionID(), data.getCharacterID());

                if (character instanceof PlayerCharacter) {
                    PlayerCharacter player = (PlayerCharacter) character;

                    ICurrency newCurrency = player.getCurrency().sub(data.getCurrency());

                    if (newCurrency.getCoinValue() > 0) {
                        player.setCurrency(newCurrency);

                        manager.getNetworkHandler().broadcast(new CurrencyUpdateNotificationMessage(
                                new CurrencyData(player.getCharacterID(), newCurrency),
                                calendar.getTime()
                        ), manager.getNetworkHandler().getClientsWithCharacterControl(data.getCharacterID()));
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
