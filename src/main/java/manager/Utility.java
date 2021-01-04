package manager;

import model.Rarity;
import model.item.Item;
import model.loot.Loot;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import ui.utility.MemoryView;

import java.util.*;

public abstract class Utility {

    public static MemoryView memoryView;

    private static final Random rand = new Random();
    private static Configuration config = null;


    static {
        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
                new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                        .configure(params.properties()
                                .setFileName("Configuration.properties")
                                .setListDelimiterHandler(new DefaultListDelimiterHandler(',')));

        try {
            config = builder.getConfiguration();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static Configuration getConfig() {
        return config;
    }


    /**
     * Generates a string that represents
     * a rarity of the database
     *
     * @return a rarity matching the chance
     */
    public static Rarity getRandomRarity() {
        double percent = rand.nextDouble();

        return Arrays.stream(Rarity.values())
                .sorted(Comparator.comparingDouble(Rarity::getChance))
                .filter(r -> percent < r.getChance())
                .findFirst().orElse(Rarity.unknown);
    }

    /**
     * Generates a random number in between 1 and the maximal tier specified in the config
     * with a higher chance to generate a low number.
     *
     * @return a random generated Tier
     */
    public static int getRandomTier() {
        double percent = rand.nextDouble();

        Float[] array = (Float[]) config.getArray(Float.class, "tier.chance");

        for (int i = array.length - 1; i >= 0; i--) {
            if (percent < array[i]) {
                return i + 1;
            }
        }

        return 1;
    }

    /**
     * Converts an int into a representation
     * in a roman number
     *
     * @param x which gets converted
     * @return a string that represents the matching roman number
     */
    public static String asRomanNumber(int x) {
        String in = String.valueOf(x);
        StringBuilder out = new StringBuilder();

        for (int i = 0; i < in.length(); i++) {
            String[] symbols = getSymbols((int) Math.pow(10, i));
            int digit = Integer.parseInt(String.valueOf(in.charAt(in.length() - 1 - i)));

            if (digit == 4) {
                out.insert(0, symbols[0] + symbols[1]);
                continue;
            }
            if (digit == 9) {
                out.insert(0, symbols[0] + symbols[2]);
                continue;
            }
            if (digit > 4) {
                for (int j = 5; j < digit; j++) {
                    out.insert(0, symbols[0]);
                }
                out.insert(0, symbols[1]);
                continue;
            }
            for (int j = 0; j < digit; j++) {
                out.insert(0, symbols[0]);
            }
        }
        return out.toString();
    }

    private static String[] getSymbols(int pot) {
        switch (pot) {
            case 1:
                return new String[]{"I", "V", "X"};
            case 10:
                return new String[]{"X", "L", "C"};
            case 100:
                return new String[]{"C", "D", "M"};
            default:
                return new String[]{"", "", ""};
        }
    }

    /**
     * Visualises the amount of copper coins
     * in copper, silver and gold
     *
     * @param cost amount of copper coins
     * @return human-readable format
     */
    public static String visualiseSell(int cost) {
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

    public static int sellLoot(Collection<Loot> loot) {
        int itemValue = 0;
        int coinValue = 0;

        int silverToCopper = Utility.getConfig().getInt("coin.silver.toCopper");
        int goldToCopper = Utility.getConfig().getInt("coin.gold.toSilver") * silverToCopper;
        String copper = LanguageUtility.getMessage("coin.copper");
        String silver = LanguageUtility.getMessage("coin.silver");
        String gold = LanguageUtility.getMessage("coin.gold");

        for (Loot l : loot) {
            Item item = l.getItem();

            if (item.getCostOfOneAsCopper() > 0) {
                itemValue += item.getCostOfOneAsCopper() * l.getAmount();
            } else {
                String itemName = item.getName();
                if (itemName.equalsIgnoreCase(copper)) {
                    coinValue += l.getAmount();
                } else if (itemName.equalsIgnoreCase(silver)) {
                    coinValue += l.getAmount() * silverToCopper;
                } else if (itemName.equalsIgnoreCase(gold)) {
                    coinValue += l.getAmount() * goldToCopper;
                }
            }
        }

        return Math.round(itemValue * 0.8f) + coinValue;
    }

    /**
     * Reads the cost from the String and
     * returns is a a amount of copper coins
     *
     * @param cost String to read
     * @return amount of copper coins
     */
    public static int toCopperCost(String cost) {
        if (cost.isBlank()) {
            return 0;
        }

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
     * Reads and consumes all chars from the input list
     * until if finds a non digit character
     *
     * @param input list that gets consumed
     * @return the parsed digits as int
     */
    public static int parseNumber(List<Character> input) {
        StringBuilder number = new StringBuilder();

        while (!input.isEmpty()) {
            char c = input.get(0);

            if (Character.isDigit(c)) {
                input.remove(0);
                number.append(c);
            } else {
                break;
            }
        }

        try {
            return Integer.parseInt(number.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Reads and consumes all chars from the input list
     * until if finds a digit character
     *
     * @param input list that gets consumed
     * @return the parsed chars as trimmed String
     */
    public static String parseString(List<Character> input) {
        StringBuilder result = new StringBuilder();

        while (!input.isEmpty()) {
            char c = input.get(0);

            if (!Character.isDigit(c)) {
                input.remove(0);
                result.append(c);
            } else {
                break;
            }
        }

        return result.toString().trim();
    }
}
