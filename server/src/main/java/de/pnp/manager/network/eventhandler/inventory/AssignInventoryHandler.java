package de.pnp.manager.network.eventhandler.inventory;

import de.pnp.manager.model.other.IContainer;
import de.pnp.manager.network.interfaces.Client;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.message.inventory.AssignInventoryMessage;
import de.pnp.manager.network.state.INonConditionalEventHandler;

import java.util.stream.Collectors;

public class AssignInventoryHandler implements INonConditionalEventHandler<BaseMessage> {

    protected Client client;

    public AssignInventoryHandler(Client client) {
        this.client = client;
    }

    @Override
    public void applyNonConditionalEvent(BaseMessage message) {
        if (message instanceof AssignInventoryMessage) {
            AssignInventoryMessage assignMessage = (AssignInventoryMessage) message;

            client.getAccessibleInventories().addAll(assignMessage.getData().stream()
                    .map(IContainer::getInventoryID).collect(Collectors.toList()));
        }
    }
}
