package de.pnp.manager.component;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Represents multiple dices which get thrown multiple times.
 */
public class Dice {

    @NotNull
    @JsonProperty("dices")
    private final List<@NotNull SingleDice> dices;

    @JsonCreator
    public Dice(List<SingleDice> dices) {
        this.dices = dices;
    }

    /**
     * Throws the dice and returns the result of the throw.
     */
    public int throwTheDice(Random random) {
        int result = 0;

        for (SingleDice dice : dices) {
            result += dice.throwTheDice(random);
        }

        return result;
    }

    /**
     * Returns the dice in a human-readable format.
     */
    public String toHumandReadableString() {
        return dices.stream().map(SingleDice::toHumandReadableString).collect(Collectors.joining(" + "));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Dice dice = (Dice) o;
        return Objects.equals(dices, dice.dices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dices);
    }

    /**
     * Returns a simple {@link Dice}.
     */
    public static Dice simpleDice(int dice) {
        return new Dice(List.of(new SingleDice(1, dice)));
    }

    /**
     * Represents a single Dice multiple times.
     */
    public record SingleDice(@Positive int numberOfThrows, @Positive int dice) {

        private int throwTheDice(Random random) {
            int result = 0;

            for (int i = 0; i < numberOfThrows; i++) {
                result += random.nextInt(dice) + 1;
            }

            return result;
        }

        private String toHumandReadableString() {
            if (numberOfThrows == 1) {
                return "D" + dice;
            }
            return numberOfThrows + " D" + dice;
        }
    }
}
