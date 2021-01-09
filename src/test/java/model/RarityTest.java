package model;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class RarityTest {

    @Before
    public void setup() {
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

        assertTrue("Common", rarityMap.get(Rarity.common) > 7500 && rarityMap.get(Rarity.common) < 8500);
        assertTrue("Rare", rarityMap.get(Rarity.rare) > 1000 && rarityMap.get(Rarity.rare) < 2000);
        assertTrue("Epic", rarityMap.get(Rarity.epic) > 200 && rarityMap.get(Rarity.epic) < 600);
        assertTrue("Legendary", rarityMap.get(Rarity.legendary) > 50 && rarityMap.get(Rarity.legendary) < 150);
        assertTrue("Godlike", rarityMap.get(Rarity.godlike) == 0);
        assertTrue("Unknown", rarityMap.get(Rarity.unknown) == 0);
    }
}
