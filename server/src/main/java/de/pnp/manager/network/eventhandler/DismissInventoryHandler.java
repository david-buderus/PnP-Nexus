package de.pnp.manager.network.eventhandler;

import de.pnp.manager.network.interfaces.Client;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.message.inventory.DismissInventoriesMessage;
import de.pnp.manager.network.state.IEventHandler;
import de.pnp.manager.network.state.INonConditionalEventHandler;

public class DismissInventoryHandler implements IEventHandler<BaseMessage>, INonConditionalEventHandler<BaseMessage> {

    protected Client client;

    public DismissInventoryHandler(Client client) {
        this.client = client;
    }

    @Override
    public void applyNonConditionalEvent(BaseMessage message) {
        if (message instanceof DismissInventoriesMessage) {
            DismissInventoriesMessage dismissMessage = (DismissInventoriesMessage) message;
            client.getAccessibleInventories().removeAll(dismissMessage.getData());
        }
    }
}
