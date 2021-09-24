package de.pnp.manager.app.state

object ConfigHelper {
    fun getInt(key: String) : Int {
        return ApplicationState.currentSession!!.config[key] as Int
    }
}