package de.pnp.manager.app.model

import de.pnp.manager.model.ICurrency
import de.pnp.manager.model.IRarity
import de.pnp.manager.model.item.IPlant
import java.util.*

class Plant : IPlant, Item() {
    private var locations: MutableCollection<String> = Collections.emptyList()

    override fun copy(): IPlant {
        TODO("Not yet implemented")
    }

    override fun getLocations(): MutableCollection<String> {
        return locations
    }

    override fun setLocations(locations: MutableCollection<String>) {
        this.locations = locations
    }

}