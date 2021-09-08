package model;

import manager.LanguageUtility;
import manager.Utility;
import org.apache.commons.configuration2.Configuration;

import java.util.ArrayList;
import java.util.List;

import static manager.Utility.consumeNumber;
import static manager.Utility.consumeString;

/**
 * The coin value is defined in copper coins
 */
public class Currency implements ICurrency {

    protected final int coinValue;
    protected final String coinString;
    protected final boolean tradeable;

    /**
     * Represents a not tradeable currency object
     */
    public Currency() {
        this.tradeable = false;
        this.coinValue = 0;
        this.coinString = LanguageUtility.getMessage("coin.notTradeable");
    }

    /**
     * Creates a currency object with a predefined number of coins
     *
     * @param coinValue amount of  copper coins
     */
    public Currency(int coinValue) {
        this.tradeable = true;
        this.coinValue = coinValue;
        this.coinString = toCoinString(coinValue);
    }

    /**
     * Creates a currency object which matches the value
     * defined in the coinString
     *
     * @param coinString in the format of 7G 3S 11K
     */
    public Currency(String coinString) {
        if (coinString.equalsIgnoreCase(LanguageUtility.getMessage("coin.notTradeable"))) {
            this.tradeable = false;
            this.coinValue = 0;
            this.coinString = LanguageUtility.getMessage("coin.notTradeable");
        } else {
            this.tradeable = true;
            this.coinString = coinString;
            this.coinValue = toCoinValue(coinString);
        }
    }

    public Currency(int copper, int silver, int gold) {
        this.tradeable = true;
        Configuration config = Utility.getConfig();
        int silverToCopper = config.getInt("coin.silver.toCopper");
        int goldToCopper = config.getInt("coin.gold.toSilver") * silverToCopper;

        this.coinValue = copper + silverToCopper * silver + goldToCopper * gold;
        this.coinString = toCoinString(coinValue);
    }

    @Override
    public ICurrency add(ICurrency other) {
        return new Currency(getCoinValue() + other.getCoinValue());
    }

    @Override
    public ICurrency add(int value) {
        return new Currency(getCoinValue() + value);
    }

    @Override
    public ICurrency sub(ICurrency other) {
        return new Currency(getCoinValue() - other.getCoinValue());
    }

    @Override
    public ICurrency sub(int value) {
        return new Currency(getCoinValue() - value);
    }

    @Override
    public ICurrency multiply(float multiplicative) {
        return new Currency(Math.round(getCoinValue() * multiplicative));
    }

    @Override
    public ICurrency divide(float d) {
        return new Currency(Math.round(getCoinValue() / d));
    }

    /**
     * Reads the cost from the String and
     * returns is a a amount of copper coins
     *
     * @param cost String to read
     * @return amount of copper coins
     */
    protected int toCoinValue(String cost) {
        if (cost.isBlank()) {
            return 0;
        }
        Configuration config = Utility.getConfig();

        int value = 0;
        int silverToCopper = config.getInt("coin.silver.toCopper");
        int goldToCopper = config.getInt("coin.gold.toSilver") * silverToCopper;
        String copper = LanguageUtility.getMessage("coin.copper.short");
        String silver = LanguageUtility.getMessage("coin.silver.short");
        String gold = LanguageUtility.getMessage("coin.gold.short");

        List<Character> costList = new ArrayList<>();
        for (char c : cost.toCharArray()) {
            costList.add(c);
        }

        while (!costList.isEmpty()) {
            int amount = consumeNumber(costList);
            String coin = consumeString(costList);

            if (coin.equals(copper)) {
                value += amount;
            } else if (coin.equals(silver)) {
                value += amount * silverToCopper;
            } else if (coin.equals(gold)) {
                value += amount * goldToCopper;
            }
        }

        return value;
    }

    /**
     * Visualises the amount of copper coins
     * in copper, silver and gold
     *
     * @param cost amount of copper coins
     * @return human-readable format
     */
    protected static String toCoinString(int cost) {
        int silverToCopper = Utility.getConfig().getInt("coin.silver.toCopper");
        int goldToSilver = Utility.getConfig().getInt("coin.gold.toSilver");
        String copperCoin = LanguageUtility.getMessage("coin.copper.short");
        String silverCoin = LanguageUtility.getMessage("coin.silver.short");
        String goldCoin = LanguageUtility.getMessage("coin.gold.short");

        int copper = cost % silverToCopper;
        cost /= silverToCopper;
        int silver = cost % goldToSilver;
        cost /= goldToSilver;
        int gold = cost;

        StringBuilder result = new StringBuilder();

        if (gold > 0) {
            result.append(gold).append(goldCoin).append(" ");
        }
        if (silver > 0) {
            result.append(silver).append(silverCoin).append(" ");
        }
        if (copper > 0) {
            result.append(copper).append(copperCoin);
        }

        if (result.length() == 0) {
            return "0" + copperCoin;
        }

        return result.toString().trim();
    }

    /**
     * The amount of copper coins this currency object represents
     */
    public int getCoinValue() {
        return coinValue;
    }

    /**
     * A human readable String of the value that this object represents
     */
    public String getCoinString() {
        return coinString;
    }

    /**
     * If this currency object represents a tradeable value
     */
    public boolean isTradeable() {
        return tradeable;
    }

    @Override
    public String toString() {
        if (tradeable) {
            return getCoinString();
        } else {
            return LanguageUtility.getMessage("coin.notTradeable");
        }
    }
}
