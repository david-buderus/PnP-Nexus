package de.pnp.manager.app.model

import de.pnp.manager.model.ICurrency
import de.pnp.manager.model.IRarity
import de.pnp.manager.model.item.IEquipment
import de.pnp.manager.model.item.IJewellery
import de.pnp.manager.model.upgrade.IUpgrade
import java.util.ArrayList

class Jewellery: IJewellery, Equipment() {
    private var gem : String = ""

    override fun copy(): IJewellery {
        TODO("Not yet implemented")
    }

    override fun getGem(): String {
        return gem
    }

    override fun setGem(gem: String) {
        this.gem = gem
    }

}