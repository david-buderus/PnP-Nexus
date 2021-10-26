package de.pnp.manager.network.message.character.currency;

import de.pnp.manager.model.ICurrency;
import de.pnp.manager.network.message.DataMessage;

import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.CREATE_CURRENCY_REQUEST;

public class CreateCurrencyRequestMessage extends DataMessage<CurrencyData> {

    public CreateCurrencyRequestMessage() {
    }

    public CreateCurrencyRequestMessage(String characterID, ICurrency currency, Date timestamp) {
        super(CREATE_CURRENCY_REQUEST, timestamp);
        this.setData(new CurrencyData(characterID, currency));
    }
}
