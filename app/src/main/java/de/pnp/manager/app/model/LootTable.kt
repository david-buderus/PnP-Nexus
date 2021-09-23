package de.pnp.manager.app.model

import de.pnp.manager.model.ICurrency
import de.pnp.manager.model.item.IItem
import de.pnp.manager.model.loot.ILoot
import de.pnp.manager.model.loot.ILootFactory
import de.pnp.manager.model.loot.ILootTable

class LootTable : ILootTable {
    override fun add(name: String?, amount: Int, chance: Double) {
        TODO("Not yet implemented")
    }

    override fun add(factory: ILootFactory?) {
        TODO("Not yet implemented")
    }

    override fun add(currency: ICurrency?) {
        TODO("Not yet implemented")
    }

    override fun add(item: IItem?, amount: Int, chance: Double) {
        TODO("Not yet implemented")
    }

    override fun add(other: ILootTable?) {
        TODO("Not yet implemented")
    }

    override fun getLootFactories(): MutableCollection<ILootFactory> {
        TODO("Not yet implemented")
    }

    override fun setLootFactories(factories: MutableCollection<ILootFactory>?) {
        TODO("Not yet implemented")
    }

    override fun getLoot(): MutableCollection<ILoot> {
        TODO("Not yet implemented")
    }
}