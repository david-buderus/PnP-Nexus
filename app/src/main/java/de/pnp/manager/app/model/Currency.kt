package de.pnp.manager.app.model

import de.pnp.manager.model.ICurrency

class Currency() : ICurrency {

    private var coinValue = 0
    private var coinString: String = ""
    protected var tradeable = false

    constructor(valueAsString: String): this() {

    }

    constructor(copperValue : Int) : this() {
        coinValue = copperValue
    }

    init {
        this.tradeable = false
        this.coinValue = 0
        this.coinString = "" //TODO LanguageUtility.getMessage("coin.notTradeable")
    }

    override fun add(other: ICurrency?): ICurrency {
        return Currency(getCoinValue() + other!!.coinValue)
    }

    override fun add(value: Int): ICurrency {
        return Currency(getCoinValue() + value)
    }

    override fun sub(other: ICurrency?): ICurrency {
        return Currency(getCoinValue() - other!!.coinValue)
    }

    override fun sub(value: Int): ICurrency {
        return Currency(getCoinValue() - value)
    }

    override fun multiply(multiplicative: Float): ICurrency {
        return Currency(Math.round(getCoinValue() * multiplicative))
    }

    override fun divide(d: Float): ICurrency {
        return Currency(Math.round(getCoinValue() / d))
    }

    override fun getCoinValue(): Int {
        TODO("Not yet implemented")
    }

    override fun getCoinString(): String {
        TODO("Not yet implemented")
    }

    override fun isTradeable(): Boolean {
        TODO("Not yet implemented")
    }
}