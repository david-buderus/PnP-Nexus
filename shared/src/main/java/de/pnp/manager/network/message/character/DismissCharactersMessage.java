package de.pnp.manager.network.message.character;

import de.pnp.manager.network.message.DataMessage;

import java.util.Collection;
import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.DISMISS_CHARACTERS;

public class DismissCharactersMessage extends DataMessage<Collection<String>> {

    public DismissCharactersMessage() {
    }

    public DismissCharactersMessage(Collection<String> characterIDs, Date timestamp) {
        super(DISMISS_CHARACTERS, timestamp);
        this.setData(characterIDs);
    }
}
