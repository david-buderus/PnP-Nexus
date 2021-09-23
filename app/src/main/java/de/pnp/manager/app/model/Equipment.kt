package de.pnp.manager.app.model

import de.pnp.manager.model.ICurrency
import de.pnp.manager.model.item.IEquipment
import de.pnp.manager.model.upgrade.IUpgrade

open class Equipment : IEquipment, Item() {
    private var wearTick : Int = 0
    private var wearStage : Int = 0
    private var material : String = ""
    private var upgrades : ArrayList<IUpgrade> = ArrayList()
    private var upgradeSlots : Int = 0

    override fun getCurrency(): ICurrency? {
        var currency = super.getCurrency()

        for (upgrade in upgrades) {
            currency = currency!!.add(upgrade.cost)
        }

        return currency
    }

    override fun copy(): IEquipment {
        TODO("Not yet implemented")
    }

    override fun applyWear(wear: Int) {
        setWearTick(getWearTick() + wear)
/*        if (shouldBreak()) {
            for (listener in this.onBreakListeners) {
                listener.accept(this)
            }
        }*/
    }

    override fun getWithUpgrade(): IEquipment {
        TODO("Not yet implemented")
    }

    override fun getMaterial(): String {
        return material
    }

    override fun setMaterial(material: String) {
        this.material = material
    }

    override fun getUpgradeSlots(): Int {
        return upgradeSlots
    }

    override fun setUpgradeSlots(upgradeSlots: Int) {
        this.upgradeSlots = upgradeSlots
    }

    override fun getUpgrades(): MutableCollection<IUpgrade> {
        return upgrades
    }

    override fun setUpgrades(upgrades: ArrayList<IUpgrade>) {
        this.upgrades = upgrades
    }

    override fun getWearStage(): Int {
        return wearStage
    }

    fun setWearStage(value: Int) {
        this.wearStage = value
    }

    override fun getWearTick(): Int {
        return wearTick
    }

    override fun setWearTick(wearTick: Int) {
        this.wearTick = wearTick
    }
}