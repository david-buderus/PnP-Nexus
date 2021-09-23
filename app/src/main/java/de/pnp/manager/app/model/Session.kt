package de.pnp.manager.app.model

import de.pnp.manager.network.client.IClient
import de.pnp.manager.network.session.ISession
import java.util.*

class Session : ISession {

    private var sessionID: String = ""
    fun setSessionId(value : String) {
        sessionID = value
    }
    override fun getSessionID() = sessionID

    private var sessionName: String = ""
    fun setSessionName(value : String) {
        sessionName = value
    }
    override fun getSessionName() = sessionName

    private var maxClients: Int = 0
    fun setMaxClients(value : Int) {
        maxClients = value
    }
    override fun getMaxClients() = maxClients

    private var passwordProtected: Boolean = false
    fun setPasswordProtected(value : Boolean) {
        passwordProtected = value
    }
    override fun isPasswordProtected() = passwordProtected

    private var host: String = ""
    fun setHost(value : String) {
        host = value
    }
    override fun getHost() = host

    private var info: String = ""
    fun setInfo(value : String) {
        info = value
    }
    override fun getInfo() = info

    private var participatingClients: MutableCollection<out IClient> = Collections.emptyList()
    fun setParticipatingClients(value : MutableCollection<out IClient>) {
        participatingClients = value
    }
    override fun getParticipatingClients() = participatingClients

    private var config: MutableMap<String, Any> = Collections.emptyMap()
    fun setConfig(value : MutableMap<String, Any>) {
        config = value
    }
    override fun getConfig() = config
}