package de.pnp.manager.network.eventhandler;

import de.pnp.manager.network.interfaces.Client;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.message.character.DismissCharactersMessage;
import de.pnp.manager.network.state.IEventHandler;

public class DismissCharacterHandler implements IEventHandler<BaseMessage> {

    protected Client client;

    public DismissCharacterHandler(Client client) {
        this.client = client;
    }

    @Override
    public boolean applyEvent(BaseMessage message) {
        if (!(message instanceof DismissCharactersMessage)) {
            return false;
        }

        DismissCharactersMessage dismissCharactersMessage = (DismissCharactersMessage) message;

        if (dismissCharactersMessage.getData().isEmpty()) {
            return false;
        }

        this.client.getControlledCharacters().removeAll(dismissCharactersMessage.getData());

        return this.client.getControlledCharacters().isEmpty();
    }
}
