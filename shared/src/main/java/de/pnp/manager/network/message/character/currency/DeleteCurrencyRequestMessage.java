package de.pnp.manager.network.message.character.currency;

import de.pnp.manager.model.ICurrency;
import de.pnp.manager.network.message.DataMessage;

import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.CREATE_CURRENCY_REQUEST;
import static de.pnp.manager.network.message.MessageIDs.DELETE_CURRENCY_REQUEST;

public class DeleteCurrencyRequestMessage extends DataMessage<CurrencyData> {

    public DeleteCurrencyRequestMessage() {
    }

    public DeleteCurrencyRequestMessage(String characterID, ICurrency currency, Date timestamp) {
        super(DELETE_CURRENCY_REQUEST, timestamp);
        this.setData(new CurrencyData(characterID, currency));
    }
}
