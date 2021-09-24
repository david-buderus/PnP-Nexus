package de.pnp.manager.app.state

object ConfigHelper {
    fun getInt(key: String) : Int {
        return Integer.parseInt(ApplicationState.currentSession!!.config[key] as String)
    }
}