package de.pnp.manager.component.character;

import de.pnp.manager.component.inventory.Inventory;

import java.util.Map;
import java.util.Objects;

/**
 * The inventory of a {@link PnPCharacter}.
 */
public class CharacterInventory {

    private final Map<String, Inventory> inventories;

    private int coin;

    public CharacterInventory(Map<String, Inventory> inventories, int coin) {
        this.inventories = inventories;
        this.coin = coin;
    }

    public Map<String, Inventory> getInventories() {
        return inventories;
    }

    public int getCoin() {
        return coin;
    }

    /**
     * Returns the inventory with the given name
     */
    public Inventory getInventory(String name) {
        return inventories.get(name);
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CharacterInventory that = (CharacterInventory) o;
        return coin == that.coin && Objects.equals(inventories, that.inventories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inventories, coin);
    }
}
