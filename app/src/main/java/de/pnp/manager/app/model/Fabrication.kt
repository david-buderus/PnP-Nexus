package de.pnp.manager.app.model

import de.pnp.manager.model.IFabrication
import de.pnp.manager.model.IItemList
import de.pnp.manager.model.item.IItem

class Fabrication : IFabrication {
    private var product : IItem? = null
    private var profession : String = ""
    private var requirement: String = ""
    private var otherCircumstances : String = ""
    private var productAmount: Float = 0f
    private var sideProduct : IItem? = null
    private var sideProductAmount: Float = 0f
    private var materials: IItemList? = null

    override fun getProduct(): IItem? {
        return this.product
    }

    override fun setProduct(product: IItem?) {
        this.product = product
    }

    override fun getProfession(): String {
        return this.profession
    }

    override fun setProfession(profession: String) {
        this.profession = profession
    }

    override fun getRequirement(): String {
        return requirement
    }

    override fun setRequirement(requirement: String) {
        this.requirement = requirement
    }

    override fun getOtherCircumstances(): String {
        return otherCircumstances
    }

    override fun setOtherCircumstances(otherCircumstances: String) {
        this.otherCircumstances = otherCircumstances
    }

    override fun getProductAmount(): Float {
        return productAmount
    }

    override fun setProductAmount(productAmount: Float) {
        this.productAmount = productAmount
    }

    override fun getSideProduct(): IItem? {
        return sideProduct
    }

    override fun setSideProduct(sideProduct: IItem?) {
        this.sideProduct = sideProduct
    }

    override fun getSideProductAmount(): Float {
        return this.sideProductAmount
    }

    override fun setSideProductAmount(sideProductAmount: Float) {
        this.sideProductAmount = sideProductAmount
    }

    override fun getMaterials(): IItemList? {
        return materials
    }

    override fun setMaterials(materials: IItemList?) {
        this.materials = materials
    }
}