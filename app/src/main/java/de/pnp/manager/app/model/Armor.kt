package de.pnp.manager.app.model

import de.pnp.manager.model.ICurrency
import de.pnp.manager.model.IRarity
import de.pnp.manager.model.item.IArmor
import de.pnp.manager.model.item.IEquipment
import de.pnp.manager.model.upgrade.IUpgrade
import java.util.ArrayList

class Armor : IArmor {
    override fun getName(): String {
        TODO("Not yet implemented")
    }

    override fun setName(name: String?) {
        TODO("Not yet implemented")
    }

    override fun setType(type: String?) {
        TODO("Not yet implemented")
    }

    override fun getType(): String {
        TODO("Not yet implemented")
    }

    override fun getCurrency(): ICurrency {
        TODO("Not yet implemented")
    }

    override fun setCurrency(currency: ICurrency?) {
        TODO("Not yet implemented")
    }

    override fun getSubtype(): String {
        TODO("Not yet implemented")
    }

    override fun setSubtype(subtype: String?) {
        TODO("Not yet implemented")
    }

    override fun getRequirement(): String {
        TODO("Not yet implemented")
    }

    override fun setRequirement(requirement: String?) {
        TODO("Not yet implemented")
    }

    override fun getEffect(): String {
        TODO("Not yet implemented")
    }

    override fun setEffect(effect: String?) {
        TODO("Not yet implemented")
    }

    override fun getRarity(): IRarity {
        TODO("Not yet implemented")
    }

    override fun setRarity(rarity: IRarity?) {
        TODO("Not yet implemented")
    }

    override fun getTier(): Int {
        TODO("Not yet implemented")
    }

    override fun setTier(tier: Int) {
        TODO("Not yet implemented")
    }

    override fun getAmount(): Float {
        TODO("Not yet implemented")
    }

    override fun getPrettyAmount(): String {
        TODO("Not yet implemented")
    }

    override fun setAmount(amount: Float) {
        TODO("Not yet implemented")
    }

    override fun addAmount(amount: Float) {
        TODO("Not yet implemented")
    }

    override fun getCurrencyWithAmount(): ICurrency {
        TODO("Not yet implemented")
    }

    override fun isTradeable(): Boolean {
        TODO("Not yet implemented")
    }

    override fun copy(): IArmor {
        TODO("Not yet implemented")
    }

    override fun applyWear(wear: Int) {
        TODO("Not yet implemented")
    }

    override fun getWithUpgrade(): IEquipment {
        TODO("Not yet implemented")
    }

    override fun getMaterial(): String {
        TODO("Not yet implemented")
    }

    override fun setMaterial(material: String?) {
        TODO("Not yet implemented")
    }

    override fun getUpgradeSlots(): Int {
        TODO("Not yet implemented")
    }

    override fun setUpgradeSlots(upgradeSlots: Int) {
        TODO("Not yet implemented")
    }

    override fun getUpgrades(): MutableCollection<IUpgrade> {
        TODO("Not yet implemented")
    }

    override fun setUpgrades(upgrades: ArrayList<IUpgrade>?) {
        TODO("Not yet implemented")
    }

    override fun getWearStage(): Int {
        TODO("Not yet implemented")
    }

    override fun getWearTick(): Int {
        TODO("Not yet implemented")
    }

    override fun setWearTick(wearTick: Int) {
        TODO("Not yet implemented")
    }

    override fun getProtection(): Int {
        TODO("Not yet implemented")
    }

    override fun setProtection(protection: Int) {
        TODO("Not yet implemented")
    }

    override fun getProtectionWithWear(): Int {
        TODO("Not yet implemented")
    }

    override fun getWeight(): Double {
        TODO("Not yet implemented")
    }

    override fun setWeight(weight: Double) {
        TODO("Not yet implemented")
    }
}