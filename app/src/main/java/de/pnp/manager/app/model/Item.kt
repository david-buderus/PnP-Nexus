package de.pnp.manager.app.model

import de.pnp.manager.model.ICurrency
import de.pnp.manager.model.IRarity
import de.pnp.manager.model.item.IItem

open class Item : IItem {
    private var name: String? = null
    private var type: String? = null
    private var subtype: String? = null
    private var requirement: String? = null
    private var effect: String? = null
    private var rarity: IRarity? = null
    private var currency: ICurrency? = null
    private var tier: Int = 0
    private var amount: Float = 0.0f
    private var tradeable: Boolean = false

    override fun getName(): String? {
        return name
    }

    override fun setName(value: String?) {
        name = value
    }

    override fun getType(): String? {
        return type
    }

    override fun setType(value: String?) {
        type = value
    }

    override fun getSubtype(): String? {
        return subtype
    }

    override fun setSubtype(value: String?) {
        subtype = value
    }

    override fun getRequirement(): String? {
        return requirement
    }

    override fun setRequirement(value: String?) {
        requirement = value
    }

    override fun getEffect(): String? {
        return effect
    }

    override fun setEffect(value: String?) {
        effect = value
    }

    override fun getRarity(): IRarity? {
        return rarity
    }

    override fun setRarity(value: IRarity?) {
        rarity = value
    }

    override fun getCurrency(): ICurrency? {
        return currency
    }

    override fun setCurrency(value: ICurrency) {
        currency = value
    }

    override fun getTier(): Int {
        return tier
    }

    override fun setTier(value: Int) {
        tier = value
    }

    override fun getAmount(): Float {
        return amount
    }

    override fun setAmount(value: Float) {
        amount = value
    }

    override fun addAmount(amount: Float) {
        this.amount += amount
    }

    override fun getCurrencyWithAmount(): ICurrency {
        return this.currency?.multiply(this.amount) ?: Currency()
    }

    override fun isTradeable(): Boolean {
        return tradeable
    }

    fun setTradeable(value: Boolean) {
        this.tradeable = value
    }

    override fun copy(): IItem {
        val item = this.javaClass.getConstructor().newInstance()
        item.amount = this.amount
        item.currency = this.currency
        item.effect = this.effect
        item.isTradeable = this.isTradeable
        item.name = this.name
        item.rarity = this.rarity
        item.requirement = this.requirement
        item.subtype = this.subtype
        item.tier = this.tier
        item.type = this.type

        return item
    }
}