import { Universe } from "../api";

/**
 * Returns the currency as human-readable string.
 * 
 * For example as 2G 11S 9K
 */
export function currencyToHumanReadable(universe: Universe, amount: number): string {
    if (universe === null) {
        return amount.toString();
    }

    const currencyCalculation = universe.settings.currencyCalculation;

    if (amount <= 0) {
        return "0" + currencyCalculation.baseCurrencyShortForm;
    }

    let remainingAmount = amount;
    let result = "";
    let previousCoin = currencyCalculation.baseCurrencyShortForm;

    for (const entry of currencyCalculation.calculationEntries) {
        const amountOfPreviousCoin = remainingAmount % entry.factor;
        if (amountOfPreviousCoin > 0) {
            result = amountOfPreviousCoin + previousCoin + " " + result;
        }
        remainingAmount /= entry.factor;
        previousCoin = entry.currencyShortForm;

        if (remainingAmount <= 0) {
            break;
        }
    }

    return result;
}
