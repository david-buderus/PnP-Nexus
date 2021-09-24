package de.pnp.manager.app.model

import de.pnp.manager.model.ICurrency
import de.pnp.manager.model.IRarity
import de.pnp.manager.model.item.IArmor
import de.pnp.manager.model.item.IEquipment
import de.pnp.manager.model.upgrade.IUpgrade
import java.util.ArrayList

class Armor : IArmor, Equipment() {
    private var protection: Int = 0
    private var weight: Double = 0.0

    override fun copy(): IArmor {
        val armor = super.copy() as IArmor
        armor.protection = this.protection
        armor.weight = this.weight
        return armor
    }

    override fun getProtection(): Int {
        return this.protection
    }

    override fun setProtection(protection: Int) {
        this.protection = protection
    }

    override fun getProtectionWithWear(): Int {
        TODO("Not yet implemented")
    }

    override fun getWeight(): Double {
        return this.weight
    }

    override fun setWeight(weight: Double) {
       this.weight = weight
    }

}