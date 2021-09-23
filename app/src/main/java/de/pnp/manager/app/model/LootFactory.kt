package de.pnp.manager.app.model

import de.pnp.manager.model.item.IItem
import de.pnp.manager.model.loot.ILoot
import de.pnp.manager.model.loot.ILootFactory
import java.util.*

class LootFactory : ILootFactory {
    override fun getItem(): IItem {
        TODO("Not yet implemented")
    }

    override fun setItem(item: IItem?) {
        TODO("Not yet implemented")
    }

    override fun getMaxAmount(): Int {
        TODO("Not yet implemented")
    }

    override fun setMaxAmount(maxAmount: Int) {
        TODO("Not yet implemented")
    }

    override fun getChance(): Double {
        TODO("Not yet implemented")
    }

    override fun setChance(chance: Double) {
        TODO("Not yet implemented")
    }

    override fun getLoot(): ILoot {
        TODO("Not yet implemented")
    }

    override fun getLoot(random: Random?): ILoot {
        TODO("Not yet implemented")
    }
}