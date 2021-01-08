package model;

import manager.LanguageUtility;
import manager.Utility;
import org.apache.commons.configuration2.Configuration;

import java.util.ArrayList;
import java.util.List;

import static manager.Utility.parseNumber;
import static manager.Utility.parseString;

public class Currency {

    private int coinValue;
    private String coinString;
    private final boolean tradeable;

    public Currency() {
        this.tradeable = false;
        this.setCoinValue(0);
    }

    public Currency(int coinValue) {
        this.setCoinValue(coinValue);
        this.tradeable = true;
    }

    public Currency(String coinString) {
        if (coinString.equalsIgnoreCase(LanguageUtility.getMessage("coin.notTradeable"))) {
            this.setCoinValue(0);
            this.tradeable = false;
        } else {
            this.setCoinString(coinString);
            this.tradeable = true;
        }
    }

    public Currency add(Currency other) {
        return new Currency(getCoinValue() + other.getCoinValue());
    }

    public Currency add(int value) {
        return new Currency(getCoinValue() + value);
    }

    public Currency sub(Currency other) {
        return new Currency(getCoinValue() - other.getCoinValue());
    }

    public Currency sub(int value) {
        return new Currency(getCoinValue() - value);
    }

    public Currency multiply(float multiplicative) {
        return new Currency(Math.round(getCoinValue() * multiplicative));
    }

    public Currency divide(float d) {
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
            int amount = parseNumber(costList);
            String coin = parseString(costList);

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

    public int getCoinValue() {
        return coinValue;
    }

    public void setCoinValue(int coinValue) {
        this.coinValue = coinValue;
        this.coinString = toCoinString(coinValue);
    }

    public String getCoinString() {
        return coinString;
    }

    public void setCoinString(String coinString) {
        this.coinString = coinString;
        this.coinValue = toCoinValue(coinString);
    }

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
