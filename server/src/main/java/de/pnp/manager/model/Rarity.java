package de.pnp.manager.model;

import de.pnp.manager.main.Utility;
import de.pnp.manager.model.interfaces.WithUnlocalizedName;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Random;

public enum Rarity implements IRarity, WithUnlocalizedName {
    UNKNOWN("unknown"), COMMON("common"), RARE("rare"),
    EPIC("epic"), LEGENDARY("legendary"), GODLIKE("godlike");

    private static final Random rand = new Random();

    private final String keyName;

    Rarity(String keyName) {
        this.keyName = keyName;
    }

    @Override
    public String getUnlocalizedName() {
        return "rarity." + keyName;
    }

    @Override
    public Rarity getLowerRarity() {
        switch (this) {
            case UNKNOWN:
                return UNKNOWN;
            case RARE:
                return COMMON;
            case EPIC:
                return RARE;
            case LEGENDARY:
                return EPIC;
            case GODLIKE:
                return LEGENDARY;
        }
        return COMMON;
    }

    public int getWeight() {
        if (this == UNKNOWN) {
            return 0;
        }
        return Utility.getConfig().getInt("rarity.weight." + keyName);
    }

    @Override
    public String toString() {
        return toStringProperty().get();
    }


    /**
     * Generates a string that represents
     * a rarity of the database
     *
     * @return a rarity matching the chance
     */
    public static Rarity getRandomRarity() {
        int weightSum = Arrays.stream(Rarity.values()).mapToInt(Rarity::getWeight).sum();
        int weight = rand.nextInt(weightSum);

        for (Rarity rarity : Rarity.values()) {
            if (weight < rarity.getWeight()) {
                return rarity;
            }
            weight -= rarity.getWeight();
        }

        return COMMON;
    }

    public static Rarity getRarity(String name) {
        for (Rarity rarity : values()) {
            if (rarity.toStringProperty().get().equalsIgnoreCase(name.trim())) {
                return rarity;
            }
        }
        throw new NoSuchElementException("The Rarity with the name " + name + " does not exists.");
    }
}
