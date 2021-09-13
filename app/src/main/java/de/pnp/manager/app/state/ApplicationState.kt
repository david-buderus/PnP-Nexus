package de.pnp.manager.app.state

import de.pnp.manager.network.client.IClient
import de.pnp.manager.network.session.ISession
import de.pnp.manager.network.session.SessionInfo

object ApplicationState {
    var client : IClient? = null
    val sessions : ArrayList<SessionInfo> = ArrayList()
}