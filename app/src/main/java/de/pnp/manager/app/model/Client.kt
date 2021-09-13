package de.pnp.manager.app.model

import de.pnp.manager.network.client.IClient

class Client(private var clientID: String, private var clientName: String) : IClient {

    override fun getClientID(): String {
        return clientID
    }

    override fun getClientName(): String {
        return clientName
    }
}