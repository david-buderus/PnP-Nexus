package de.pnp.manager.component;

import static org.assertj.core.api.Assertions.assertThat;

import de.pnp.manager.component.Dice.SingleDice;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Dice}
 */
class DiceTest {

    @Test
    void throwTheDice() {
        Random diceRandom = new Random(10);
        Random testRandom = new Random(10);

        Dice dice = new Dice(List.of(new SingleDice(3, 6), new SingleDice(1, 4)));

        int expectedResult =
            testRandom.nextInt(6) + testRandom.nextInt(6) + testRandom.nextInt(6) + testRandom.nextInt(4) + 4;

        assertThat(dice.throwTheDice(diceRandom)).isEqualTo(expectedResult);
    }

    @Test
    void toHumanReadableString() {
        assertThat(new Dice(List.of(new SingleDice(3, 6), new SingleDice(1, 4))).toHumandReadableString()).isEqualTo(
            "3 D6 + D4");
    }
}