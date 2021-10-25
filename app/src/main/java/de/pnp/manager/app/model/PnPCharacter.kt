package de.pnp.manager.app.model

import de.pnp.manager.model.character.IInventory
import de.pnp.manager.model.character.IPnPCharacter
import de.pnp.manager.model.character.Inventory
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
import kotlin.collections.HashMap

open class PnPCharacter() : IPnPCharacter {
    private var name: String = ""
    private var characterID : String = ""
    private var level: Int = 0
    private var advantages : MutableCollection<String> = ArrayList()
    private var disadvantages : MutableCollection<String> = ArrayList()
    private var primaryAttributes : MutableMap<IPrimaryAttribute, Int> = HashMap()
    private var secondaryAttributes : MutableMap<ISecondaryAttribute, Int> = HashMap()
    private var weapons : MutableCollection<IWeapon> = ArrayList()
    private var equippedWeapons : MutableCollection<IWeapon> = ArrayList()
    private var equippedJewellery : MutableCollection<IJewellery> = ArrayList()
    private var equippedArmor : MutableMap<IArmorPosition, IArmor> = HashMap()
    private var spells : MutableCollection<ISpell> = ArrayList()
    private var inventory : IInventory? = null
    private var talents : MutableMap<ITalent, Int> = HashMap()
    private var lootTable : ILootTable? = null
    private var memberStates : MutableCollection<IMemberState> = ArrayList()

    private var baseDefense: Int = 0
    private var initiative: Int = 0
    private var health: Int = 0
    private var mana: Int = 0
    private var mentalHealth : Int = 0

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
        return health
    }

    override fun getMaxHealth(): Int {
        return secondaryAttributes[SecondaryAttribute.HEALTH]?:0
    }

    fun setMaxHealth(maxHealth: Int) {
        secondaryAttributes[SecondaryAttribute.HEALTH] = maxHealth
    }
    override fun getMana(): Int {
        return mana
    }

    override fun getMaxMana(): Int {
        return secondaryAttributes[SecondaryAttribute.MANA]?:0
    }

    fun setMaxMana(maxMana: Int) {
        println("setting maxMana")
        println(" " + secondaryAttributes + " " + secondaryAttributes[SecondaryAttribute.MANA] + " " + maxMana)
        println(maxMana.javaClass)
        secondaryAttributes.put(SecondaryAttribute.MANA, maxMana)
    }

    override fun getMentalHealth(): Int {
        return mentalHealth
    }

    override fun getMaxMentalHealth(): Int {
        return secondaryAttributes[SecondaryAttribute.MENTAL_HEALTH]?: 0
    }

    fun setMaxMentalHealth(maxMentalHealth : Int) {
        this.secondaryAttributes[SecondaryAttribute.MENTAL_HEALTH] = maxMentalHealth
    }

    override fun getInitiative(): Int {
        return initiative
    }

    override fun getBaseDefense(): Int {
       return this.baseDefense
    }

    fun setBaseDefense(baseDefense : Int) {
        this.baseDefense = baseDefense
    }

    override fun getPrimaryAttributes(): MutableMap<IPrimaryAttribute, Int> {
        return primaryAttributes
    }

    fun setPrimaryAttributes(primaryAttributes: MutableMap<IPrimaryAttribute, Int>) {
        this.primaryAttributes = primaryAttributes
    }

    override fun getSecondaryAttributes(): MutableMap<ISecondaryAttribute, Int> {
        return secondaryAttributes
    }

    fun setSecondaryAttributes(secondaryAttributes : MutableMap<ISecondaryAttribute, Int>) {
        this.secondaryAttributes = secondaryAttributes
    }

    override fun getMemberStates(): MutableCollection<IMemberState> {
        return memberStates
    }

    fun setMemberStates(memberStates : MutableCollection<IMemberState>) {
        this.memberStates = memberStates
    }

    override fun getWeapons(): MutableCollection<IWeapon> {
        return weapons
    }

    fun setWeapons(weapons: MutableCollection<IWeapon>) {
        this.weapons = weapons
    }

    override fun getEquippedWeapons(): MutableCollection<IWeapon> {
        return equippedWeapons
    }

    fun setEquippedWeapons(equippedWeapons: MutableCollection<IWeapon>) {
        this.equippedWeapons = equippedWeapons
    }

    override fun getEquippedJewellery(): MutableCollection<IJewellery> {
        return equippedJewellery
    }

    fun setEquippedJewellery(equippedJewellery:  MutableCollection<IJewellery>) {
        this.equippedJewellery = equippedJewellery
    }

    override fun getEquippedArmor(): MutableMap<IArmorPosition, IArmor> {
        return equippedArmor
    }

    fun setEquippedArmor(armor: MutableMap<IArmorPosition, IArmor>) {
        this.equippedArmor = armor
    }

    override fun getSpells(): MutableCollection<ISpell> {
        return spells
    }

    fun setSpells(spells: MutableCollection<ISpell>) {
        this.spells = spells
    }

    override fun getTalents(): MutableMap<ITalent, Int> {
        return talents
    }

    fun setTalents(talents: MutableMap<ITalent, Int>) {
        this.talents = talents
    }

    override fun getInventory(): IInventory {
        return inventory?:Inventory.createEmptyInventory()
    }

    fun setInventory(inventory: IInventory) {
        this.inventory = inventory
    }

    override fun getLootTable(): ILootTable {
        return this.getLootTable()
    }

    fun setLootTable(lootTable: LootTable) {
        this.lootTable = lootTable
    }
}