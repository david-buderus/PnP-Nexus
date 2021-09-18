package de.pnp.manager.network.eventhandler;

import de.pnp.manager.model.character.IPnPCharacter;
import de.pnp.manager.network.interfaces.Client;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.message.character.AssignCharactersMessage;
import de.pnp.manager.network.state.IEventHandler;

import java.util.stream.Collectors;

public class AssignCharacterHandler implements IEventHandler<BaseMessage> {

    protected Client client;

    public AssignCharacterHandler(Client client) {
        this.client = client;
    }

    @Override
    public boolean applyEvent(BaseMessage message) {
        if (!(message instanceof AssignCharactersMessage)) {
            return false;
        }

        AssignCharactersMessage assignCharactersMessage = (AssignCharactersMessage) message;

        if (assignCharactersMessage.getData().isEmpty()) {
            return false;
        }
        client.getControlledCharacters().addAll(assignCharactersMessage.getData().stream()
                .map(IPnPCharacter::getCharacterID).collect(Collectors.toList()));

        return true;
    }
}
