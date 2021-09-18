package de.pnp.manager.network.message.character;

import de.pnp.manager.model.character.IPnPCharacter;
import de.pnp.manager.network.message.DataMessage;

import java.util.Collection;
import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.ASSIGN_CHARACTERS;

public class AssignCharactersMessage extends DataMessage<Collection<IPnPCharacter>> {

    public AssignCharactersMessage() {
    }

    public AssignCharactersMessage(Collection<IPnPCharacter> characters, Date timestamp) {
        super(ASSIGN_CHARACTERS, timestamp);
        this.setData(characters);
    }
}
