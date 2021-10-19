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
        this(new FabricateItemData(inventoryID, fabrication), timestamp);
    }

    public FabricateItemRequestMessage(FabricateItemData data, Date timestamp) {
        super(FABRICATE_ITEM_REQUEST, timestamp);
        this.setData(data);
    }

    public static class FabricateItemData {
        protected String inventoryID;
        protected IFabrication fabrication;

        public FabricateItemData() {
        }

        public FabricateItemData(String inventoryID, IFabrication fabrication) {
            this.inventoryID = inventoryID;
            this.fabrication = fabrication;
        }

        public String getInventoryID() {
            return inventoryID;
        }

        public void setInventoryID(String inventoryID) {
            this.inventoryID = inventoryID;
        }

        public IFabrication getFabrication() {
            return fabrication;
        }

        public void setFabrication(IFabrication fabrication) {
            this.fabrication = fabrication;
        }
    }
}
