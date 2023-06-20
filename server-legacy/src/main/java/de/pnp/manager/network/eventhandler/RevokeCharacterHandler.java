package de.pnp.manager.network.eventhandler;

import de.pnp.manager.network.interfaces.Client;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.message.character.RevokeCharactersMessage;
import de.pnp.manager.network.state.IEventHandler;

public class RevokeCharacterHandler implements IEventHandler<BaseMessage> {

    protected Client client;

    public RevokeCharacterHandler(Client client) {
        this.client = client;
    }

    @Override
    public boolean applyEvent(BaseMessage message) {
        if (!(message instanceof RevokeCharactersMessage)) {
            return false;
        }

        RevokeCharactersMessage revokeCharactersMessage = (RevokeCharactersMessage) message;

        if (revokeCharactersMessage.getData().isEmpty()) {
            return false;
        }

        this.client.getControlledCharacters().removeAll(revokeCharactersMessage.getData());

        return this.client.getControlledCharacters().isEmpty();
    }
}
