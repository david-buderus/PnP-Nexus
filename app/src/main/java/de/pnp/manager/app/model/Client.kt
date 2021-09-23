package de.pnp.manager.app.model

import de.pnp.manager.network.client.IClient

class Client(private var clientID: String, private var clientName: String) : IClient {

    constructor() : this("", "")

    override fun getClientID(): String {
        return clientID
    }

    fun setClientID(value : String) {
        clientID = value
    }

    override fun getClientName(): String {
        return clientName
    }

    fun setClientName(value: String) {
        clientName = value
    }
}