package model;

import javafx.beans.property.ReadOnlyStringProperty;
import manager.LanguageUtility;
import manager.Utility;
import model.interfaces.WithToStringProperty;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Random;

public enum Rarity implements WithToStringProperty {
    unknown, common, rare, epic, legendary, godlike;

    private static final Random rand = new Random();

    @Override
    public ReadOnlyStringProperty toStringProperty() {
        return LanguageUtility.getMessageProperty("rarity." + super.toString());
    }

    public int getWeight() {
        if (this == unknown) {
            return 0;
        }
        return Utility.getConfig().getInt("rarity.weight." + super.toString());
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

        return common;
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
