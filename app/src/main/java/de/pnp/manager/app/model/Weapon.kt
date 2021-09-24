package de.pnp.manager.app.model

import de.pnp.manager.model.ICurrency
import de.pnp.manager.model.IRarity
import de.pnp.manager.model.item.IEquipment
import de.pnp.manager.model.item.IWeapon
import de.pnp.manager.model.upgrade.IUpgrade
import java.util.ArrayList

class Weapon : IWeapon, Equipment() {
    private var isShield: Boolean = false
    private var initiative: Float = 0.0f
    private var dice: String = ""
    private var damage: Int = 0
    private var hit: Int = 0
    private var initiativeModifier: Float = 0.0f

    override fun copy(): IWeapon {
        val weapon = super.copy() as Weapon
        weapon.damage = this.damage
        weapon.isShield = this.isShield
        weapon.initiative = this.initiative
        weapon.dice = this.dice
        weapon.hit = this.hit
        weapon.initiativeModifier = this.initiativeModifier
        return weapon
    }

    override fun isShield(): Boolean {
        return isShield
    }

    override fun getInitiativeModifier(): Float {
        return this.initiativeModifier
    }

    override fun setInitiativeModifier(initiativeModifier: Float) {
        this.initiativeModifier = initiativeModifier
    }

    fun setIsShield(value: Boolean) {
        this.isShield = value
    }

    override fun getInitiative(): Float {
        return initiative
    }

    override fun setInitiative(initiative: Float) {
        this.initiative = initiative
    }

    override fun getDice(): String {
        return dice
    }

    override fun setDice(dice: String) {
        this.dice = dice
    }

    override fun getDamage(): Int {
        return damage
    }

    override fun setDamage(damage: Int) {
        this.damage = damage
    }

    override fun getHit(): Int {
        return hit
    }

    override fun setHit(hit: Int) {
        this.hit = hit
    }
}