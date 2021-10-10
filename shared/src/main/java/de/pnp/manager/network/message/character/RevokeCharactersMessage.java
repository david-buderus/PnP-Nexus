package de.pnp.manager.network.message.character;

import de.pnp.manager.network.message.DataMessage;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.REVOKE_CHARACTERS;

public class RevokeCharactersMessage extends DataMessage<Collection<String>> {

    public RevokeCharactersMessage() {
    }

    public RevokeCharactersMessage(String characterID, Date timestamp) {
        this(Collections.singletonList(characterID), timestamp);
    }

    public RevokeCharactersMessage(Collection<String> characterIDs, Date timestamp) {
        super(REVOKE_CHARACTERS, timestamp);
        this.setData(characterIDs);
    }
}
