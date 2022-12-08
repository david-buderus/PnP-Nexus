package de.pnp.manager.main;

import de.pnp.manager.model.Currency;
import de.pnp.manager.model.ICurrency;
import de.pnp.manager.model.item.IItem;
import de.pnp.manager.model.loot.ILoot;
import de.pnp.manager.ui.utility.MemoryView;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;

public abstract class Utility {

    public static MemoryView memoryView;

    private static final Random rand = new Random();
    private static Configuration config = null;

    private static final String[] romanNumerals = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
    private static final int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};

    static {
        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
                new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                        .configure(params.properties()
                                .setFileName("Configuration.properties")
                                .setListDelimiterHandler(new DefaultListDelimiterHandler(',')));

        try {
            config = builder.getConfiguration();

            // Load external configuration file
            Path home = Paths.get(System.getProperty("user.home"), config.getString("home.folder"), "Configuration.properties");

            if (home.toFile().exists()) {
                builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                        .configure(params.properties()
                                .setFile(home.toFile())
                                .setListDelimiterHandler(new DefaultListDelimiterHandler(',')));

                Configuration localConfig = builder.getConfiguration();
                for (Iterator<String> it = localConfig.getKeys(); it.hasNext(); ) {
                    String key = it.next();
                    config.setProperty(key, localConfig.getProperty(key));
                }
            }
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static Configuration getConfig() {
        return config;
    }

    /**
     * Generates a random number in between 1 and the maximal tier specified in the config
     * with a higher chance to generate a low number.
     *
     * @return a random generated Tier
     */
    public static int getRandomTier() {
        Integer[] array = (Integer[]) config.getArray(Integer.class, "tier.weight");
        double weight = rand.nextInt(Arrays.stream(array).mapToInt(i -> i).sum());


        for (int i = array.length - 1; i >= 0; i--) {
            if (weight < array[i]) {
                return i + 1;
            }
            weight -= array[i];
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
        StringBuilder roman = new StringBuilder();

        for (int i = 0; i < romanNumerals.length; i++) {
            while (x >= values[i]) {
                roman.append(romanNumerals[i]);
                x -= values[i];
            }
        }

        return roman.toString();
    }

    public static ICurrency sellLoot(Collection<ILoot> loot) {
        ICurrency itemsSellingPrice = new Currency();
        ICurrency valueOfTheCoins = new Currency();
        String currencyString = LanguageUtility.getMessage("currency");

        for (ILoot l : loot) {
            IItem item = l.getItem();


            if (item.getSubtype().equalsIgnoreCase(currencyString)) {
                valueOfTheCoins = valueOfTheCoins.add(item.getCurrencyWithAmount().multiply(l.getAmount()));
            } else {
                itemsSellingPrice = itemsSellingPrice.add(item.getCurrencyWithAmount().multiply(l.getAmount()));

            }
        }

        return itemsSellingPrice.multiply(config.getFloat("loot.sell.modifier")).add(valueOfTheCoins);
    }

    /**
     * Reads and consumes all chars from the input list
     * until if finds a non digit character
     *
     * @param input list that gets consumed
     * @return the parsed digits as int
     */
    public static int consumeNumber(List<Character> input) {
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
    public static String consumeString(List<Character> input) {
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

    public static void saveToCustomConfig(String key, Object object) {

        Path home = Paths.get(System.getProperty("user.home"), config.getString("home.folder"), "Configuration.properties");

        try {
            if (home.toFile().getParentFile().mkdirs() && home.toFile().createNewFile()) {
                Logger.getLogger("Utility").info("Custom Configuration.properties created");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
                new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                        .configure(params.properties()
                                .setFile(home.toFile())
                                .setListDelimiterHandler(new DefaultListDelimiterHandler(',')));

        try {
            Configuration customConfig = builder.getConfiguration();
            customConfig.setProperty(key, object);

            builder.save();

        } catch (ConfigurationException e) {
            e.printStackTrace();
        }

        config.setProperty(key, object);
    }
}
