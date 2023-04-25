package de.pnp.manager.network.message.character.currency;

import de.pnp.manager.model.ICurrency;

public class CurrencyData {

    protected String characterID;
    protected ICurrency currency;

    public CurrencyData() {
    }

    public CurrencyData(String characterID, ICurrency currency) {
        this.characterID = characterID;
        this.currency = currency;
    }

    public String getCharacterID() {
        return characterID;
    }

    public void setCharacterID(String characterID) {
        this.characterID = characterID;
    }

    public ICurrency getCurrency() {
        return currency;
    }

    public void setCurrency(ICurrency currency) {
        this.currency = currency;
    }
}
