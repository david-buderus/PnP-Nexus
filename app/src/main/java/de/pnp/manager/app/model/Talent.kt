package de.pnp.manager.app.model

import de.pnp.manager.model.character.data.IPrimaryAttribute
import de.pnp.manager.model.other.ITalent

class Talent : ITalent {
    private var name : String = ""
    private var attributes : Array<IPrimaryAttribute> = Array(0){PrimaryAttribute.DUMMY}
    private var magicTalent : Boolean = false
    private var weaponTalent: Boolean = false

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
        return this.magicTalent
    }

    override fun setMagicTalent(magicTalent: Boolean) {
        this.magicTalent = magicTalent
    }

    override fun isWeaponTalent(): Boolean {
        return weaponTalent
    }

    override fun setWeaponTalent(weaponTalent: Boolean) {
        this.weaponTalent = weaponTalent
    }
}