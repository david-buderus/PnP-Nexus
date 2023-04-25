package de.pnp.manager.network.message.character.currency;

import de.pnp.manager.model.item.IItem;
import de.pnp.manager.network.message.DataMessage;

import java.util.Collection;
import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.MOVE_CURRENCY_FROM_INVENTORY_REQUEST;

public class MoveCurrencyFromInventoryRequestMessage extends DataMessage<MoveCurrencyFromInventoryRequestMessage.MoveCurrencyFromInventoryData> {

    public MoveCurrencyFromInventoryRequestMessage() {
    }

    public MoveCurrencyFromInventoryRequestMessage(String from, String to, Collection<? extends IItem> currencyItems, Date timestamp) {
        super(MOVE_CURRENCY_FROM_INVENTORY_REQUEST, timestamp);
        this.setData(new MoveCurrencyFromInventoryData(from, to, currencyItems));
    }

    public static class MoveCurrencyFromInventoryData {

        protected String from;
        protected String to;
        protected Collection<? extends IItem> currencyItems;

        public MoveCurrencyFromInventoryData() {
        }

        public MoveCurrencyFromInventoryData(String from, String to, Collection<? extends IItem> currencyItems) {
            this.from = from;
            this.to = to;
            this.currencyItems = currencyItems;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public Collection<? extends IItem> getCurrencyItems() {
            return currencyItems;
        }

        public void setCurrencyItems(Collection<? extends IItem> currencyItems) {
            this.currencyItems = currencyItems;
        }
    }
}
