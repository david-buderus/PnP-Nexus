package de.pnp.manager.component.character;

import de.pnp.manager.component.inventory.Inventory;

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
}
