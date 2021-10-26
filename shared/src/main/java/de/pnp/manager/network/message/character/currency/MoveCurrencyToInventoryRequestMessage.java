package de.pnp.manager.network.message.character.currency;

import de.pnp.manager.model.ICurrency;
import de.pnp.manager.network.message.DataMessage;

import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.MOVE_CURRENCY_TO_INVENTORY_REQUEST;

public class MoveCurrencyToInventoryRequestMessage extends DataMessage<MoveCurrencyData> {

    public MoveCurrencyToInventoryRequestMessage() {
    }

    public MoveCurrencyToInventoryRequestMessage(String from, String to, ICurrency currency, Date timestamp) {
        super(MOVE_CURRENCY_TO_INVENTORY_REQUEST, timestamp);
        this.setData(new MoveCurrencyData(from, to, currency));
    }

}