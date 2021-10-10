package de.pnp.manager.app.model

import de.pnp.manager.model.IRarity

enum class Rarity : IRarity {
    UNKNOWN, COMMON, RARE, EPIC, LEGENDARY, GODLIKE;

    override fun getLowerRarity(): IRarity {
        return when(this) {
            UNKNOWN -> UNKNOWN
            COMMON -> COMMON
            RARE -> COMMON
            EPIC -> RARE
            LEGENDARY -> EPIC
            GODLIKE -> LEGENDARY
        }
    }
}