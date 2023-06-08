package de.pnp.manager.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RarityTest {

    @BeforeAll
    public static void setup() {
    }

    @Test
    public void testRarityChance() {
        Map<Rarity, Integer> rarityMap = new HashMap<>();

        for (Rarity r : Rarity.values()) {
            rarityMap.put(r, 0);
        }

        for (int i = 0; i < 10000; i++) {
            Rarity randomRarity = Rarity.getRandomRarity();
            rarityMap.put(randomRarity, rarityMap.get(randomRarity) + 1);
        }

        assertTrue(rarityMap.get(Rarity.COMMON) > 7500 && rarityMap.get(Rarity.COMMON) < 8500);
        assertTrue(rarityMap.get(Rarity.RARE) > 1000 && rarityMap.get(Rarity.RARE) < 2000);
        assertTrue(rarityMap.get(Rarity.EPIC) > 200 && rarityMap.get(Rarity.EPIC) < 600);
        assertTrue(rarityMap.get(Rarity.LEGENDARY) > 50 && rarityMap.get(Rarity.LEGENDARY) < 150);
        assertTrue(rarityMap.get(Rarity.GODLIKE) == 0);
        assertTrue(rarityMap.get(Rarity.UNKNOWN) == 0);
    }
}
