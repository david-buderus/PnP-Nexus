package de.pnp.manager.app.model

import de.pnp.manager.model.character.data.IPrimaryAttribute

enum class PrimaryAttribute : IPrimaryAttribute {
     STRENGTH, ENDURANCE, DEXTERITY, INTELLIGENCE, CHARISMA, RESILIENCE, AGILITY, PRECISION, DUMMY;

    override fun toShortString(): String {
        TODO("Not yet implemented")
    }
}