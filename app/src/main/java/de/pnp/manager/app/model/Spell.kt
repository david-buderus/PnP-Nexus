package de.pnp.manager.app.model

import de.pnp.manager.model.other.ISpell
import de.pnp.manager.model.other.ITalent

class Spell : ISpell {
    private var tier: Int = 0
    private var name: String? = null
    private var effect: String? = null
    private var type: String? = null
    private var cost: String? = null
    private var castTime: String? = null
    private var talent: ITalent? = null

    override fun getTier(): Int {
        return tier
    }

    override fun setTier(tier: Int) {
        this.tier = tier
    }

    override fun getName(): String {
        return this.name!!
    }

    override fun setName(name: String?) {
        this.name = name
    }

    override fun getEffect(): String {
        return this.effect!!
    }

    override fun setEffect(effect: String?) {
        this.effect = effect
    }

    override fun getType(): String {
        return this.type!!
    }

    override fun setType(type: String?) {
        this.type = type
    }

    override fun getCost(): String {
        return cost!!
    }

    override fun setCost(cost: String?) {
        this.cost = cost
    }

    override fun getCastTime(): String {
        return this.castTime!!
    }

    override fun setCastTime(castTime: String?) {
       this.castTime = castTime
    }

    override fun getTalent(): ITalent {
        return talent!!
    }

    override fun setTalent(talent: ITalent?) {
        this.talent = talent
    }
}