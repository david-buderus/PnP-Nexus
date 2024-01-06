package de.pnp.manager.component.universe;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;
import java.util.Objects;

/**
 * Defines how the vendor prices are displayed to the user.
 */
public class CurrencyCalculation {

    @NotBlank
    private final String baseCurrency;

    @NotBlank
    private final String baseCurrencyShortForm;

    @NotNull
    private final List<@Valid CurrencyCalculationEntry> calculationEntries;

    public CurrencyCalculation(String baseCurrency, String baseCurrencyShortForm,
        List<CurrencyCalculationEntry> calculationEntries) {
        this.baseCurrency = baseCurrency;
        this.baseCurrencyShortForm = baseCurrencyShortForm;
        this.calculationEntries = calculationEntries;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public String getBaseCurrencyShortForm() {
        return baseCurrencyShortForm;
    }

    public List<CurrencyCalculationEntry> getCalculationEntries() {
        return calculationEntries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CurrencyCalculation that = (CurrencyCalculation) o;
        return getBaseCurrency().equals(that.getBaseCurrency()) && getBaseCurrencyShortForm().equals(
            that.getBaseCurrencyShortForm()) && getCalculationEntries().equals(that.getCalculationEntries());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBaseCurrency(), getBaseCurrencyShortForm(), getCalculationEntries());
    }

    /**
     * Defines how many coins are needed to calculate this currency.
     */
    public record CurrencyCalculationEntry(@Positive int factor, @NotBlank String currency,
                                           @NotBlank String currencyShortForm) {

    }
}
