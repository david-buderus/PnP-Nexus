package de.pnp.manager.app.state

import de.pnp.manager.app.network.MessageHandler
import de.pnp.manager.model.character.IPnPCharacter
import de.pnp.manager.network.client.IClient
import de.pnp.manager.network.message.database.DatabaseResponseMessage
import de.pnp.manager.network.session.ISession
import de.pnp.manager.network.session.SessionInfo

object ApplicationState {
    var client : IClient? = null
    val messageHandler = MessageHandler()
    val sessions : ArrayList<SessionInfo> = ArrayList()
    var currentSession: ISession? = null
    var databaseData : DatabaseResponseMessage.DatabaseData? = null
    var myCharacters : Collection<IPnPCharacter>? = null
}