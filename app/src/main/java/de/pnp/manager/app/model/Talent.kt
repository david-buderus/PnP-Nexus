package de.pnp.manager.app.model

import de.pnp.manager.model.character.data.IPrimaryAttribute
import de.pnp.manager.model.other.ITalent

class Talent : ITalent {
    private var name : String = ""
    private var attributes : Array<IPrimaryAttribute> = Array(0){PrimaryAttribute.DUMMY}
    private var isMagicTalent : Boolean = false
    private var isWeaponTalent: Boolean = false

    override fun getName(): String {
        return name
    }

    override fun setName(name: String?) {
        this.name = name?:""
    }

    override fun getAttributes(): Array<IPrimaryAttribute> {
        return attributes
    }

    override fun setAttributes(attributes: Array<IPrimaryAttribute>) {
        this.attributes = attributes
    }

    override fun isMagicTalent(): Boolean {
        return this.isMagicTalent
    }

    override fun setMagicTalent(magicTalent: Boolean) {
        this.isMagicTalent = magicTalent
    }

    override fun isWeaponTalent(): Boolean {
        return isWeaponTalent
    }

    override fun setWeaponTalent(weaponTalent: Boolean) {
        this.isWeaponTalent = weaponTalent
    }
}