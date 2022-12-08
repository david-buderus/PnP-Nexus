package de.pnp.manager.network.message.inventory;

import de.pnp.manager.model.character.IPnPCharacter;
import de.pnp.manager.model.other.IContainer;
import de.pnp.manager.network.message.DataMessage;

import java.util.Collection;
import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.*;

public class AccessibleContainerResponseMessage extends DataMessage<Collection<? extends IContainer>> {

    public AccessibleContainerResponseMessage() {
    }

    public AccessibleContainerResponseMessage(Collection<? extends IContainer> containers, Date timestamp) {
        super(ACCESSIBLE_CONTAINER_RESPONSE, timestamp);
        this.setData(containers);
    }
}
