package de.pnp.manager.network.eventhandler.currency;

import de.pnp.manager.model.ICurrency;
import de.pnp.manager.model.character.PlayerCharacter;
import de.pnp.manager.model.character.PnPCharacter;
import de.pnp.manager.model.manager.Manager;
import de.pnp.manager.network.interfaces.Client;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.message.character.currency.*;
import de.pnp.manager.network.message.error.DeniedMessage;
import de.pnp.manager.network.message.error.NotPossibleMessage;
import de.pnp.manager.network.state.INonConditionalEventHandler;

import java.util.Calendar;

import static de.pnp.manager.main.LanguageUtility.getMessage;

public class MoveCurrencyHandler implements INonConditionalEventHandler<BaseMessage> {

    protected Client client;
    protected Calendar calendar;
    protected Manager manager;

    public MoveCurrencyHandler(Client client, Calendar calendar, Manager manager) {
        this.client = client;
        this.calendar = calendar;
        this.manager = manager;
    }

    @Override
    public void applyNonConditionalEvent(BaseMessage message) {
        if (message instanceof MoveCurrencyRequestMessage) {
            MoveCurrencyData data = ((MoveCurrencyRequestMessage) message).getData();

            if (client.getControlledCharacters().contains(data.getFrom())) {
                PnPCharacter from = manager.getCharacterHandler().getCharacter(client.getSessionID(), data.getFrom());

                if (from instanceof PlayerCharacter) {
                    PlayerCharacter fromPlayer = (PlayerCharacter) from;

                    PnPCharacter to = manager.getCharacterHandler().getCharacter(client.getSessionID(), data.getTo());

                    if (to instanceof PlayerCharacter) {
                        PlayerCharacter toPlayer = (PlayerCharacter) to;

                        ICurrency newCurrencyFrom = fromPlayer.getCurrency().sub(data.getCurrency());

                        if (newCurrencyFrom.getCoinValue() > 0) {
                            fromPlayer.setCurrency(newCurrencyFrom);

                            ICurrency newCurrencyTo = toPlayer.getCurrency().add(data.getCurrency());
                            toPlayer.setCurrency(newCurrencyTo);

                            manager.getNetworkHandler().broadcast(new CurrencyUpdateNotificationMessage(
                                    new CurrencyData(data.getFrom(), newCurrencyFrom),
                                    calendar.getTime()
                            ), manager.getNetworkHandler().getClientsWithCharacterControl(data.getFrom()));

                            manager.getNetworkHandler().broadcast(new CurrencyUpdateNotificationMessage(
                                    new CurrencyData(data.getTo(), newCurrencyTo),
                                    calendar.getTime()
                            ), manager.getNetworkHandler().getClientsWithCharacterControl(data.getTo()));

                        } else {
                            client.sendMessage(new NotPossibleMessage(getMessage("message.currency.notPossible.notEnough"), calendar.getTime()));
                        }
                    } else {
                        client.sendMessage(new NotPossibleMessage(getMessage("message.currency.notPossible.notPlayer"), calendar.getTime()));
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
