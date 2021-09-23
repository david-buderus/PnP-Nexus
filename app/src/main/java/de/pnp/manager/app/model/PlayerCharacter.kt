package de.pnp.manager.app.model

import de.pnp.manager.model.ICurrency
import de.pnp.manager.model.character.IInventory
import de.pnp.manager.model.character.IPlayerCharacter
import de.pnp.manager.model.character.data.IArmorPosition
import de.pnp.manager.model.character.data.IPrimaryAttribute
import de.pnp.manager.model.character.data.ISecondaryAttribute
import de.pnp.manager.model.character.state.IMemberState
import de.pnp.manager.model.item.IArmor
import de.pnp.manager.model.item.IJewellery
import de.pnp.manager.model.item.IWeapon
import de.pnp.manager.model.loot.ILootTable
import de.pnp.manager.model.other.ISpell
import de.pnp.manager.model.other.ITalent

class PlayerCharacter : IPlayerCharacter, PnPCharacter() {
    override fun getRace(): String {
        TODO("Not yet implemented")
    }

    override fun getAge(): String {
        TODO("Not yet implemented")
    }

    override fun getExperience(): Int {
        TODO("Not yet implemented")
    }

    override fun getProfession(): String {
        TODO("Not yet implemented")
    }

    override fun getCurrency(): ICurrency {
        TODO("Not yet implemented")
    }

    override fun getHistory(): String {
        TODO("Not yet implemented")
    }

}