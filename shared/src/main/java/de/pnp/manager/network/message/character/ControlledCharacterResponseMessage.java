package de.pnp.manager.network.message.character;

import de.pnp.manager.model.character.IPnPCharacter;
import de.pnp.manager.network.message.DataMessage;

import java.util.Collection;
import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.CONTROLLED_CHARACTER_RESPONSE;

public class ControlledCharacterResponseMessage extends DataMessage<Collection<? extends IPnPCharacter>> {

    public ControlledCharacterResponseMessage() {
    }

    public ControlledCharacterResponseMessage(Collection<? extends IPnPCharacter> characters, Date timestamp) {
        super(CONTROLLED_CHARACTER_RESPONSE, timestamp);
        this.setData(characters);
    }
}
