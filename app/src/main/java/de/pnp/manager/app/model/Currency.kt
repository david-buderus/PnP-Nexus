package de.pnp.manager.app.model

import de.pnp.manager.app.state.ConfigHelper
import de.pnp.manager.model.ICurrency

class Currency() : ICurrency {

    private var coinValue = 0
    private var coinString: String = ""
    protected var tradeable = false

    constructor(valueAsString: String): this() {
        if (valueAsString.isBlank() || valueAsString.equals("Nicht Handelbar",true)) {
            return
        }

        var value = 0
        val silverToCopper: Int = ConfigHelper.getInt("coin.silver.toCopper")
        val goldToCopper: Int = ConfigHelper.getInt("coin.gold.toSilver") * silverToCopper
        val copper = "K"//val copper: String = LanguageUtility.getMessage("coin.copper.short")
        val silver = "S" //val silver: String = LanguageUtility.getMessage("coin.silver.short")
        val gold = "G"//val gold: String = LanguageUtility.getMessage("coin.gold.short")

        val costList: ArrayList<Char> = ArrayList()
        for (c in valueAsString.trim().toCharArray()) {
            costList.add(c)
        }

        while (costList.isNotEmpty()) {
            val amount: Int = consumeNumber(costList)
            val coin: String = consumeString(costList)
            if (coin == copper) {
                value += amount
            } else if (coin == silver) {
                value += amount * silverToCopper
            } else if (coin == gold) {
                value += amount * goldToCopper
            }
        }

        coinValue = value
        coinString = valueAsString
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

    fun getGoldAmount() : Int {
        val silverToCopper: Int = ConfigHelper.getInt("coin.silver.toCopper")
        val goldToSilver: Int = ConfigHelper.getInt("coin.gold.toSilver")

        var cost = coinValue
        val copper: Int = cost % silverToCopper
        cost /= silverToCopper
        val silver: Int = cost % goldToSilver
        cost /= goldToSilver
        val gold: Int = cost

        println("gold: " + gold)
        return gold
    }

    fun getSilverAmount(): Int {
        val silverToCopper: Int = ConfigHelper.getInt("coin.silver.toCopper")
        val goldToSilver: Int = ConfigHelper.getInt("coin.gold.toSilver")

        var cost = coinValue
        val copper: Int = cost % silverToCopper
        cost /= silverToCopper

        println("silver: " + cost % goldToSilver)
        return cost % goldToSilver
    }

    fun getCopperAmount(): Int {
        val silverToCopper: Int = ConfigHelper.getInt("coin.silver.toCopper")
        val goldToSilver: Int = ConfigHelper.getInt("coin.gold.toSilver")

        val cost = coinValue

        println("copper: " + cost % silverToCopper)
        return cost % silverToCopper
    }

    override fun getCoinValue(): Int {
        return this.coinValue
    }

    override fun getCoinString(): String {
        TODO("Not yet implemented")
    }

    override fun isTradeable(): Boolean {
        TODO("Not yet implemented")
    }

    /**
     * Reads and consumes all chars from the input list
     * until if finds a non digit character
     *
     * @param input list that gets consumed
     * @return the parsed digits as int
     */
    private fun consumeNumber(input: ArrayList<Char>): Int {
        val number = StringBuilder()
        while (!input.isEmpty()) {
            val c = input[0]
            if (Character.isDigit(c)) {
                input.removeAt(0)
                number.append(c)
            } else {
                break
            }
        }
        return try {
            number.toString().toInt()
        } catch (e: NumberFormatException) {
            0
        }
    }

    /**
     * Reads and consumes all chars from the input list
     * until if finds a digit character
     *
     * @param input list that gets consumed
     * @return the parsed chars as trimmed String
     */
    private fun consumeString(input: ArrayList<Char>): String {
        val result = StringBuilder()
        while (!input.isEmpty()) {
            val c = input[0]
            if (!Character.isDigit(c)) {
                input.removeAt(0)
                result.append(c)
            } else {
                break
            }
        }
        return result.toString().trim { it <= ' ' }
    }
}