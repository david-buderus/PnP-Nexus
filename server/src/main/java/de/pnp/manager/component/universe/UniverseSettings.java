package de.pnp.manager.component.universe;

import de.pnp.manager.component.universe.CurrencyCalculation.CurrencyCalculationEntry;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

/**
 * The settings of the {@link Universe}.
 */
public final class UniverseSettings {

    /**
     * The default settings
     */
    public static final UniverseSettings DEFAULT = new UniverseSettings(10, new CurrencyCalculation("Copper", "C",
        List.of(new CurrencyCalculationEntry(100, "Silver", "S"), new CurrencyCalculationEntry(100, "Gold", "G"))));

    private final int wearFactor;

    @Valid
    @NotNull
    private final CurrencyCalculation currencyCalculation;

    public UniverseSettings(int wearFactor, CurrencyCalculation currencyCalculation) {
        this.wearFactor = wearFactor;
        this.currencyCalculation = currencyCalculation;
    }

    public int getWearFactor() {
        return wearFactor;
    }

    public CurrencyCalculation getCurrencyCalculation() {
        return currencyCalculation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UniverseSettings that = (UniverseSettings) o;
        return getWearFactor() == that.getWearFactor() && Objects.equals(getCurrencyCalculation(),
            that.getCurrencyCalculation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getWearFactor(), getCurrencyCalculation());
    }
}
