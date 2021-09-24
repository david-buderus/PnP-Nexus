package de.pnp.manager.app.model

import de.pnp.manager.app.state.ConfigHelper
import de.pnp.manager.app.state.DatabaseHelper
import de.pnp.manager.model.ICurrency
import de.pnp.manager.model.item.IItem
import de.pnp.manager.model.loot.ILoot
import de.pnp.manager.model.loot.ILootFactory
import de.pnp.manager.model.loot.ILootTable

class LootTable : ILootTable {
    private var list: ArrayList<ILootFactory>? = null

    override fun add(name: String, amount: Int, chance: Double) {
        list!!.add(
            LootFactory(
                DatabaseHelper.getItem(name),
                amount,
                chance
            )
        )
    }

    override fun add(factory: ILootFactory?) {
        list!!.add(factory!!)
    }

    override fun add(currency: ICurrency?) {
        val silverToCopper: Int = ConfigHelper.getInt("coin.silver.toCopper")
        val goldToCopper: Int = ConfigHelper.getInt("coin.gold.toSilver") * silverToCopper

        var value = currency!!.coinValue
        val gp = value / goldToCopper
        value -= gp * goldToCopper

        val sp = value / silverToCopper
        value -= sp * silverToCopper

        val cp = value

        val gold: IItem =
            DatabaseHelper.getItem("Gold")
        val silver: IItem =
            DatabaseHelper.getItem("Silber")
        val copper: IItem =
            DatabaseHelper.getItem("Kupfer")

        add(gold, gp, 1.0)
        add(silver, sp, 1.0)
        add(copper, cp, 1.0)
    }

    override fun add(item: IItem?, amount: Int, chance: Double) {
        list!!.add(LootFactory(item, amount, chance))
    }

    override fun add(other: ILootTable?) {
        list!!.addAll(other!!.lootFactories)
    }

    override fun getLootFactories(): MutableCollection<ILootFactory> {
        return list!!
    }

    override fun setLootFactories(factories: MutableCollection<ILootFactory>?) {
        this.list = ArrayList(factories)
    }

    override fun getLoot(): MutableCollection<ILoot> {
        val lootList = java.util.ArrayList<ILoot>()

        for (factory in list!!) {
            val loot = factory.loot
            val own: ILoot? = getLoot(factory.item, lootList)
            if (own != null) {
                own.addAmount(loot.amount)
            } else {
                if (loot.amount > 0) {
                    lootList.add(loot)
                }
            }
        }

        return lootList
    }

    private fun getLoot(artifact: IItem, list: java.util.ArrayList<ILoot>): ILoot? {
        for (l in list) {
            if (l.item == artifact) {
                return l
            }
        }
        return null
    }
}