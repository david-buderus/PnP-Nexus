package de.pnp.manager.network.message.inventory;

import de.pnp.manager.model.other.IContainer;
import de.pnp.manager.network.message.DataMessage;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.ASSIGN_INVENTORIES;

public class AssignInventoryMessage extends DataMessage<Collection<IContainer>> {

    public AssignInventoryMessage() {
    }

    public AssignInventoryMessage(IContainer inventory, Date timestamp) {
        this(Collections.singletonList(inventory), timestamp);
    }

    public AssignInventoryMessage(Collection<IContainer> inventories, Date timestamp) {
        super(ASSIGN_INVENTORIES, timestamp);
        this.setData(inventories);
    }
}
