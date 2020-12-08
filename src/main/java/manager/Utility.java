package manager;

import city.Town;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import model.item.Armor;
import ui.utility.MemoryView;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public abstract class Utility {

    private static final Random rand = new Random();

    public static final ListProperty<Town> townList = new SimpleListProperty<>(FXCollections.observableArrayList());

    public static MemoryView memoryView;

    /**
     * Generates a string that represents
     * a rarity of the database
     *
     * @return a rarity matching the chance
     */
    public static String getRandomRarity() {
        double percent = rand.nextDouble();

        if (percent < getChanceOfRarity("legendär")) {
            return "legendär";
        }
        if (percent < getChanceOfRarity("episch")) {
            return "episch";
        }
        if (percent < getChanceOfRarity("selten")) {
            return "selten";
        }
        return "gewöhnlich";
    }

    /**
     * Returns the chance to find an item
     * of the given rarity
     *
     * @param rarity of which the chance is needed
     * @return a double in [0,1]
     */
    public static double getChanceOfRarity(String rarity) {
        if (rarity.equals("legendär")) {
            return 0.01;
        }
        if (rarity.equals("episch")) {
            return 0.05;
        }
        if (rarity.equals("selten")) {
            return 0.20;
        }
        return 1;
    }

    /**
     * Generates a Collection of String which
     * represents materials of the database.
     *
     * @return a list of materials of a specific tier
     */
    public static Collection<String> getRandomMaterial() {
        switch (getRandomTier()) {
            case 1:
                return Arrays.asList("Eisen", "Silber", "Leder", "Holz", "Papier", "Stoff");
            case 2:
                return Arrays.asList("Stahl", "Gold", "Verstärktes Leder", "Verzaubertes Holz", "Pergament", "Verzauberter Stoff");
            case 3:
                return Arrays.asList("Mithril", "Platin", "Bestienleder", "Entholz", "Verzaubertes Papier", "Seide");
            case 4:
                return Arrays.asList("Orichalcum", "Wei\u00dfgold", "Verstärktes Bestienleder", "Verzaubertes Entholz", "Verzaubertes Pergament", "Verzauberte Seide");
            case 5:
                return Arrays.asList("Adamantium", "Drachenschuppe", "Weltenholz", "Gesegnetes Pergament", "Magiestoff");
            default:
                return Collections.emptyList();
        }
    }

    /**
     * Generates a random number in [1, 5]
     * with a higher chance to generate a low number.
     *
     * @return a random generated Tier
     */
    public static int getRandomTier() {
        double percent = rand.nextDouble();

        if (percent < 0.01) {
            return 5;
        }
        if (percent < 0.1) {
            return 4;
        }
        if (percent < 0.25) {
            return 3;
        }
        if (percent < 0.5) {
            return 2;
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
        String copper = cost % 100 + "K";
        cost /= 100;
        String silver = cost % 100 + "S";
        cost /= 100;
        String gold = cost + "G";

        StringBuilder result = new StringBuilder();

        if (!gold.equals("0G")) {
            result.append(gold).append(" ");
        }
        if (!silver.equals("0S")) {
            result.append(silver).append(" ");
        }
        if (!copper.equals("0K")) {
            result.append(copper);
        }

        if (result.length() == 0) {
            return "0K";
        }

        return result.toString().trim();
    }

    /**
     * Calculates the base damage of a weapon with the specific values would have.
     *
     * @param tier       of the weapon
     * @param step       that the weapon damage does from Tier 1 to Tier 2
     * @param startValue the base damage of the weapon at Tier 1
     * @return the calculated base damage
     */
    public static int calculateWeaponDamage(int tier, int step, int startValue) {
        if (tier < 1) {
            return startValue - step;
        } else if (tier < 4) {
            return calculateWeaponDamage(tier - 1, step, startValue) + step;
        } else {
            return calculateWeaponDamage(tier - 1, step, startValue) + 2 * step;
        }
    }

    /**
     * Generates a heavy armor for the given tier and position with weight of 0.
     *
     * @param tier     of the armor
     * @param name     of the generated armor item
     * @param position of the armor
     * @return the generated armor with matching stats
     */
    public static Armor generateCommonHeavyArmor(int tier, String name, String position) {
        return generateCommonHeavyArmor(tier, name, position, 0);
    }

    /**
     * Generates a heavy armor for the given tier and position.
     *
     * @param tier     of the armor
     * @param name     of the generated armor item
     * @param position of the armor
     * @param weight   of the generated armor item
     * @return the generated armor with matching stats
     */
    public static Armor generateCommonHeavyArmor(int tier, String name, String position, int weight) {
        Armor armor = new Armor();
        armor.setName(name);
        armor.setWeight(weight);
        armor.setTier(tier);
        armor.setSubTyp(position);
        armor.setProtection(calculateHeavyArmorProtection(tier, position));
        armor.setRarity("gewöhnlich");
        return armor;
    }

    private static int calculateHeavyArmorProtection(int tier, String position) {
        switch (position) {
            case "Kopf":
            case "Arme":
                return calculateHeavyArmorProtection(tier, 3);
            case "Oberkörper":
                return calculateHeavyArmorProtection(tier, 5);
            case "Beine":
                return calculateHeavyArmorProtection(tier, 4);
        }
        return 0;
    }

    private static int calculateHeavyArmorProtection(int tier, int startValue) {
        if (tier < 1) {
            return startValue - 2;
        } else if (tier == 3) {
            return calculateHeavyArmorProtection(tier - 1, startValue);
        } else {
            return calculateHeavyArmorProtection(tier - 1, startValue) + 2;
        }
    }

    /**
     * Generates a light armor for the given tier and position with weight of 0.
     *
     * @param tier     of the armor
     * @param name     of the generated armor item
     * @param position of the armor
     * @return the generated armor with matching stats
     */
    public static Armor generateCommonLightArmor(int tier, String name, String position) {
        return generateCommonLightArmor(tier, name, position, 0);
    }

    /**
     * Generates a light armor for the given tier and position.
     *
     * @param tier     of the armor
     * @param name     of the generated armor item
     * @param position of the armor
     * @param weight   of the generated armor item
     * @return the generated armor with matching stats
     */
    public static Armor generateCommonLightArmor(int tier, String name, String position, int weight) {
        Armor armor = new Armor();
        armor.setName(name);
        armor.setWeight(weight);
        armor.setTier(tier);
        armor.setSubTyp(position);
        armor.setProtection(calculateLightArmorProtection(tier, position));
        armor.setRarity("gewöhnlich");
        return armor;
    }

    private static int calculateLightArmorProtection(int tier, String position) {
        switch (position) {
            case "Kopf":
            case "Arme":
                return calculateLightArmorProtection(tier, 1);
            case "Oberkörper":
                return calculateLightArmorProtection(tier, 3);
            case "Beine":
                return calculateLightArmorProtection(tier, 2);
        }
        return 0;
    }

    private static int calculateLightArmorProtection(int tier, int startValue) {
        if (tier < 1) {
            return startValue - 1;
        } else if (tier == 5) {
            return calculateLightArmorProtection(tier - 1, startValue) + 2;
        } else {
            return calculateLightArmorProtection(tier - 1, startValue) + 1;
        }
    }
}
