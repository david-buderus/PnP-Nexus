package de.pnp.manager.network.message.session;

import de.pnp.manager.network.message.DataMessage;
import de.pnp.manager.network.session.ISession;

import java.util.Date;

import static de.pnp.manager.network.message.MessageIDs.UPDATE_SESSION;

public class UpdateSessionMessage extends DataMessage<ISession> {

    public UpdateSessionMessage() {
    }

    public UpdateSessionMessage(ISession session, Date timestamp) {
        super(UPDATE_SESSION, timestamp);
        this.setData(session);
    }
}
