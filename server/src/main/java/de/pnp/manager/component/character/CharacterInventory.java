package de.pnp.manager.component.character;

import de.pnp.manager.component.inventory.Inventory;

import java.util.Objects;

/**
 * The inventory of a {@link PnPCharacter}.
 */
public class CharacterInventory {

    private final Inventory inventory;

    private int coin;

    public CharacterInventory(Inventory inventory, int coin) {
        this.inventory = inventory;
        this.coin = coin;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int getCoin() {
        return coin;
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
        return coin == that.coin && Objects.equals(inventory, that.inventory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inventory, coin);
    }
}
