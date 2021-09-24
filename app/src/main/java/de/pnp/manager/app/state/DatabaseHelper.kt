package de.pnp.manager.app.state

import de.pnp.manager.model.item.IItem

object DatabaseHelper {
    fun getItem(name: String) : IItem {
        return ApplicationState.databaseData!!.items!!.first { item -> item.name.equals(name) }
    }
}