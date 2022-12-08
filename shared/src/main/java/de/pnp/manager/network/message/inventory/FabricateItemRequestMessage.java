package de.pnp.manager.network.message.inventory;

import de.pnp.manager.model.IFabrication;
import de.pnp.manager.network.message.DataMessage;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.FABRICATE_ITEM_REQUEST;

public class FabricateItemRequestMessage extends DataMessage<FabricateItemRequestMessage.FabricateItemData> {

    public FabricateItemRequestMessage() {
    }

    public FabricateItemRequestMessage(String inventoryID, IFabrication fabrication, Date timestamp) {
        this(new FabricateItemData(Collections.singleton(inventoryID), inventoryID, fabrication), timestamp);
    }

    public FabricateItemRequestMessage(Collection<String> from, String to, IFabrication fabrication, Date timestamp) {
        this(new FabricateItemData(from, to, fabrication), timestamp);
    }

    public FabricateItemRequestMessage(FabricateItemData data, Date timestamp) {
        super(FABRICATE_ITEM_REQUEST, timestamp);
        this.setData(data);
    }

    public static class FabricateItemData {
        /**
         * Inventories that contain the materials.
         * It also specifies the priority of which
         * inventory the items are taken from first
         */
        protected Collection<String> from;
        /**
         * Inventory that will contain the product
         */
        protected String to;
        protected IFabrication fabrication;

        public FabricateItemData() {
        }

        public FabricateItemData(Collection<String> from, String to, IFabrication fabrication) {
            this.from = from;
            this.to = to;
            this.fabrication = fabrication;
        }

        public Collection<String> getFrom() {
            return from;
        }

        public void setFrom(Collection<String> from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public IFabrication getFabrication() {
            return fabrication;
        }

        public void setFabrication(IFabrication fabrication) {
            this.fabrication = fabrication;
        }
    }
}
