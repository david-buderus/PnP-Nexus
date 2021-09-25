package de.pnp.manager.app.state

import de.pnp.manager.model.item.IItem
import de.pnp.manager.model.other.ITalent

object DatabaseHelper {
    fun getItem(name: String) : IItem {
        return ApplicationState.databaseData!!.items!!.first { item -> item.name.equals(name, true) }
    }

    fun getTalent(name: String) : ITalent {
        println(name + " ->  " + ApplicationState.databaseData!!.talents!!.any { talent -> talent.name.equals(name, true) })
        return ApplicationState.databaseData!!.talents!!.first { talent -> talent.name.equals(name, true) }
    }
}