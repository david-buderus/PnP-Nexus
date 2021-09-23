package de.pnp.manager.app.model

import de.pnp.manager.model.character.data.IPrimaryAttribute

enum class PrimaryAttribute : IPrimaryAttribute {
     strength, endurance, dexterity, intelligence, charisma, resilience, agility, precision, DUMMY;

    override fun toShortString(): String {
        TODO("Not yet implemented")
    }
}