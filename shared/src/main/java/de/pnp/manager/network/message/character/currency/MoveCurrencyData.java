package de.pnp.manager.network.message.character.currency;

import de.pnp.manager.model.ICurrency;

public class MoveCurrencyData {
    protected String from;
    protected String to;
    protected ICurrency currency;

    public MoveCurrencyData() {
    }

    public MoveCurrencyData(String from, String to, ICurrency currency) {
        this.from = from;
        this.to = to;
        this.currency = currency;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public ICurrency getCurrency() {
        return currency;
    }

    public void setCurrency(ICurrency currency) {
        this.currency = currency;
    }
}
