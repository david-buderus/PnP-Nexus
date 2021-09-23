package de.pnp.manager.app.model

import de.pnp.manager.model.character.IInventory
import de.pnp.manager.model.character.IPnPCharacter
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
import java.util.*

open class PnPCharacter() : IPnPCharacter {
    private var name: String = ""
    private var characterID : String = ""
    private var level: Int = 0
    private var advantages : MutableCollection<String> = Collections.emptyList()
    private var disadvantages : MutableCollection<String> = Collections.emptyList()
    private var primaryAttributes : MutableMap<IPrimaryAttribute, Int> = Collections.emptyMap()
    private var secondaryAttributes : MutableMap<ISecondaryAttribute, Int> = Collections.emptyMap()
    private var weapons : MutableCollection<IWeapon> = Collections.emptyList()
    private var equippedWeapons : MutableCollection<IWeapon> = Collections.emptyList()
    private var jewellery : MutableCollection<IJewellery> = Collections.emptyList()
    private var equippedJewellery : MutableCollection<IJewellery> = Collections.emptyList()
    private var armor : MutableCollection<IArmor> = Collections.emptyList()
    private var equippedArmor : MutableMap<IArmorPosition, IArmor> = Collections.emptyMap()
    private var spells : MutableCollection<ISpell> = Collections.emptyList()
    private var inventory : IInventory? = null
    private var talents : MutableMap<ITalent, Int> = Collections.emptyMap()

    override fun getName(): String {
        return name
    }

    fun setName(value: String) {
        this.name = value
    }

    override fun getCharacterID(): String {
        return characterID
    }

    fun setCharacterID(value: String) {
        this.characterID = value
    }

    override fun getLevel(): Int {
        return level
    }

    fun setLevel(value: Int) {
        this.level = value
    }

    override fun getAdvantages(): MutableCollection<String> {
        return advantages
    }

    fun setAdvantages(value : MutableCollection<String>) {
        this.advantages = value
    }

    override fun getDisadvantages(): MutableCollection<String> {
        return disadvantages
    }

    fun setDisadvantages(value : MutableCollection<String>) {
        this.disadvantages = value
    }

    override fun getHealth(): Int {
        TODO("Not yet implemented")
    }

    override fun getMaxHealth(): Int {
        TODO("Not yet implemented")
    }

    override fun getMana(): Int {
        TODO("Not yet implemented")
    }

    override fun getMaxMana(): Int {
        TODO("Not yet implemented")
    }

    override fun getMentalHealth(): Int {
        TODO("Not yet implemented")
    }

    override fun getMaxMentalHealth(): Int {
        TODO("Not yet implemented")
    }

    override fun getInitiative(): Int {
        TODO("Not yet implemented")
    }

    override fun getStaticInitiative(): Int {
        return secondaryAttributes[SecondaryAttribute.initiative]?: 0
    }

    override fun getBaseDefense(): Int {
        TODO("Not yet implemented")
    }

    override fun getStaticBaseDefense(): Int {
        return secondaryAttributes[SecondaryAttribute.defense]?: 0
    }

    override fun getPrimaryAttributes(): MutableMap<IPrimaryAttribute, Int> {
        return primaryAttributes
    }

    override fun getSecondaryAttributes(): MutableMap<ISecondaryAttribute, Int> {
        return secondaryAttributes
    }

    override fun getMemberStates(): MutableCollection<IMemberState> {
        TODO("Not yet implemented")
    }

    override fun getWeapons(): MutableCollection<IWeapon> {
        return weapons
    }

    override fun getEquippedWeapons(): MutableCollection<IWeapon> {
        return equippedWeapons
    }

    override fun getJewellery(): MutableCollection<IJewellery> {
        return jewellery
    }

    override fun getEquippedJewellery(): MutableCollection<IJewellery> {
        return equippedJewellery
    }

    override fun getArmor(): MutableCollection<IArmor> {
        return armor
    }

    override fun getEquippedArmor(): MutableMap<IArmorPosition, IArmor> {
        return equippedArmor
    }

    override fun getSpells(): MutableCollection<ISpell> {
        return spells
    }

    override fun getTalents(): MutableMap<ITalent, Int> {
        return talents
    }

    override fun getInventory(): IInventory {
        return inventory?:Inventory()
    }

    override fun getLootTable(): ILootTable {
        TODO("Not yet implemented")
    }
}