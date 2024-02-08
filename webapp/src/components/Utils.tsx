import { Universe } from "../api";

/** Helper class to calculate human readable currency */
interface CurrencyCalculationStep {
    /** the short form of the currency */
    shortForm: string;
    /** the factor of the currency */
    factor: number;
}

/**
 * Returns the currency as human-readable string.
 * 
 * For example as 2G 11S 9K
 */
export function currencyToHumanReadable(universe: Universe, amount: number): string {
    if (Number.isNaN(amount)) {
        return "";
    }
    if (universe === null) {
        return amount.toString();
    }

    const currencyCalculation = universe.settings.currencyCalculation;

    if (amount <= 0) {
        return "0" + currencyCalculation.baseCurrencyShortForm;
    }
    if (currencyCalculation.calculationEntries.length === 0) {
        return amount + currencyCalculation.baseCurrencyShortForm;
    }

    const calcuationSteps: CurrencyCalculationStep[] = [{ shortForm: currencyCalculation.baseCurrencyShortForm, factor: 1 }];

    for (const entry of currencyCalculation.calculationEntries) {
        calcuationSteps.unshift({
            shortForm: entry.currencyShortForm,
            factor: calcuationSteps[0].factor * (entry.factor ?? 1)
        });
    }

    let result = "";
    let remainingAmount = amount;

    for (const step of calcuationSteps) {
        const coins = Math.floor(remainingAmount / step.factor);
        if (coins > 0) {
            result += " " + coins + step.shortForm;
        }
        remainingAmount %= step.factor;

        if (remainingAmount <= 0) {
            break;
        }
    }

    return result.trim();
}
