package model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

public class RarityTest {

    @BeforeAll
    public static void setup() {
    }

    @Test
    public void testRarityChance() {
        Map<Rarity, Integer> rarityMap = new HashMap<Rarity, Integer>();

        for (Rarity r : Rarity.values()) {
            rarityMap.put(r, 0);
        }

        for (int i = 0; i < 10000; i++) {
            Rarity randomRarity = Rarity.getRandomRarity();
            rarityMap.put(randomRarity, rarityMap.get(randomRarity) + 1);
        }

        assertTrue(rarityMap.get(Rarity.common) > 7500 && rarityMap.get(Rarity.common) < 8500);
        assertTrue(rarityMap.get(Rarity.rare) > 1000 && rarityMap.get(Rarity.rare) < 2000);
        assertTrue(rarityMap.get(Rarity.epic) > 200 && rarityMap.get(Rarity.epic) < 600);
        assertTrue(rarityMap.get(Rarity.legendary) > 50 && rarityMap.get(Rarity.legendary) < 150);
        assertTrue(rarityMap.get(Rarity.godlike) == 0);
        assertTrue(rarityMap.get(Rarity.unknown) == 0);
    }
}
