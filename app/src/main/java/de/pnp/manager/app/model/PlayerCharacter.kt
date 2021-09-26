package de.pnp.manager.app.model

import de.pnp.manager.app.state.ConfigHelper
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
    private var race: String = ""
    private var gender: String = ""
    private var age: String = ""
    private var experience: Int = 0
    private var profession: String = ""
    private var currency: Currency? = null
    private var history: String = ""

    fun getExperiencePercentage(): Int {
        val modifier = ConfigHelper.getInt("character.experienceLevelModifier")
        val neededXP = this.level * modifier
        return (experience / neededXP) * 100
    }

    fun getExperiencePercentageString() : String {
        val modifier = ConfigHelper.getInt("character.experienceLevelModifier")
        val neededXP = this.level * modifier
        return "$experience/$neededXP"
    }

    override fun getRace(): String {
        return race
    }

    fun setRace(race: String) {
        this.race = race
    }

    override fun getGender(): String {
        return gender
    }

    fun setGender(gender: String) {
        this.gender = gender
    }

    override fun getAge(): String {
        return age
    }

    fun setAge(age: String) {
        this.age = age
    }

    override fun getExperience(): Int {
        return experience
    }

    fun setExperience(experience: Int) {
        this.experience = experience
    }

    override fun getProfession(): String {
        return profession
    }

    fun setProfession(profession: String) {
        this.profession = profession
    }

    override fun getCurrency(): Currency? {
        return currency
    }

    fun setCurrency(currency: Currency) {
        println("currency: " + currency.coinValue)
        this.currency=  currency
    }

    override fun getHistory(): String {
        return history
    }

    fun setHistory(history: String) {
        this.history = history
    }

}