package de.pnp.manager.app.model

import de.pnp.manager.model.IRarity

enum class Rarity : IRarity {
    unknown, common, rare, epic, legendary, godlike;

    override fun getLowerRarity(): IRarity {
        return when(this) {
            unknown -> unknown
            common -> unknown
            rare -> common
            epic -> rare
            legendary -> epic
            godlike -> legendary
        }
    }
}