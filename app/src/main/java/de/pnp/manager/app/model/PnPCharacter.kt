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
    private var jewellery : MutableCollection<IJewellery> = ArrayList()
    private var equippedJewellery : MutableCollection<IJewellery> = ArrayList()
    private var armor : MutableCollection<IArmor> = ArrayList()
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
        return secondaryAttributes[SecondaryAttribute.health]?:0
    }

    fun setMaxHealth(maxHealth: Int) {
        secondaryAttributes[SecondaryAttribute.health] = maxHealth
    }
    override fun getMana(): Int {
        return mana
    }

    override fun getMaxMana(): Int {
        return secondaryAttributes[SecondaryAttribute.mana]?:0
    }

    fun setMaxMana(maxMana: Int) {
        println("setting maxMana")
        println(" " + secondaryAttributes + " " + secondaryAttributes[SecondaryAttribute.mana] + " " + maxMana)
        println(maxMana.javaClass)
        secondaryAttributes.put(SecondaryAttribute.mana, maxMana)
    }

    override fun getMentalHealth(): Int {
        return mentalHealth
    }

    override fun getMaxMentalHealth(): Int {
        return secondaryAttributes[SecondaryAttribute.mentalHealth]?: 0
    }

    fun setMaxMentalHealth(maxMentalHealth : Int) {
        this.secondaryAttributes[SecondaryAttribute.mentalHealth] = maxMentalHealth
    }

    override fun getInitiative(): Int {
        return initiative
    }

    override fun getStaticInitiative(): Int {
        return secondaryAttributes[SecondaryAttribute.initiative]?: 0
    }

    fun setStaticInitiative(staticInitiative : Int) {
        this.secondaryAttributes[SecondaryAttribute.initiative] = staticInitiative
    }

    override fun getBaseDefense(): Int {
       return this.baseDefense
    }

    fun setBaseDefense(baseDefense : Int) {
        this.baseDefense = baseDefense
    }

    override fun getStaticBaseDefense(): Int {
        return secondaryAttributes[SecondaryAttribute.defense]?: 0
    }

    fun setStaticBaseDefense(staticBaseDefense : Int) {
        this.secondaryAttributes[SecondaryAttribute.defense] = staticBaseDefense
    }

    override fun getPrimaryAttributes(): MutableMap<IPrimaryAttribute, Int> {
        return primaryAttributes
    }

    fun setPrimaryAttributes(primaryAttribute: MutableMap<IPrimaryAttribute, Int>) {
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

    override fun getJewellery(): MutableCollection<IJewellery> {
        return jewellery
    }

    fun setJewellery(jewellery:  MutableCollection<IJewellery>) {
        this.jewellery = jewellery
    }

    override fun getEquippedJewellery(): MutableCollection<IJewellery> {
        return equippedJewellery
    }

    fun setEquippedJewellery(equippedJewellery:  MutableCollection<IJewellery>) {
        this.equippedJewellery = equippedJewellery
    }

    override fun getArmor(): MutableCollection<IArmor> {
        return armor
    }

    fun setArmor(armor: MutableCollection<IArmor>) {
        this.armor = armor
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
        return inventory?:Inventory()
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