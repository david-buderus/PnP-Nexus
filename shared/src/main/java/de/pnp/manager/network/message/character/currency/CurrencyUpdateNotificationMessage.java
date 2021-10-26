package de.pnp.manager.network.message.character.currency;

import de.pnp.manager.network.message.DataMessage;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.UPDATE_CURRENCY_NOTIFICATION;

public class CurrencyUpdateNotificationMessage extends DataMessage<Collection<CurrencyData>> {

    public CurrencyUpdateNotificationMessage() {
    }

    public CurrencyUpdateNotificationMessage(CurrencyData data, Date timestamp) {
        this(Collections.singleton(data), timestamp);
    }

    public CurrencyUpdateNotificationMessage(Collection<CurrencyData> data, Date timestamp) {
        super(UPDATE_CURRENCY_NOTIFICATION, timestamp);
        this.setData(data);
    }
}
