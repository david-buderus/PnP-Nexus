package de.pnp.manager.app.model

import de.pnp.manager.model.IItemList
import de.pnp.manager.model.character.IInventory
import de.pnp.manager.model.item.IItem

class Inventory : IInventory {
    private var maxSize: Int = 0
    private var maxStackSize: Int = 0
    private var itemList : IItemList? = null

    override fun getMaxSize(): Int {
        return maxSize
    }

    fun setMaxSize(maxSize: Int) {
        this.maxSize = maxSize
    }

    override fun getMaxStackSize(): Int {
        return maxStackSize
    }

    fun setMaxStackSize(maxStackSize : Int) {
        this.maxStackSize = maxStackSize
    }

    override fun getItems(): IItemList {
        return itemList!!
    }

    fun setItems(itemList : IItemList?) {
        this.itemList = itemList
    }
}