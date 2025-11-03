import { CurrencySettings, Dice } from "../../api";
import { IResourceUsage } from "../Database";

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
export function currencyFormatter(currencySettings: CurrencySettings, rawAmount: number | string): string {
    if (currencySettings === null) {
        return rawAmount.toString();
    }
    const amount = typeof (rawAmount) === 'string' ? Number(rawAmount) : rawAmount;

    if (Number.isNaN(amount)) {
        return "";
    }

    if (amount <= 0) {
        return "0" + currencySettings.baseCurrencyShortForm;
    }
    if (currencySettings.calculationEntries.length === 0) {
        return amount + currencySettings.baseCurrencyShortForm;
    }

    const calcuationSteps: CurrencyCalculationStep[] = [{ shortForm: currencySettings.baseCurrencyShortForm, factor: 1 }];

    for (const entry of currencySettings.calculationEntries) {
        if (entry.factor <= 0) {
            continue;
        }

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
/**
 * Formats the percentage number into a human readable form.
 */
export function percentageFormatter(num: number): string {
    return new Intl.NumberFormat('default', {
        minimumFractionDigits: 0,
        maximumFractionDigits: 2,
    }).format(num * 100);
}

/**
 * Formats the number into a human readable form.
 */
export function numberFormatter(num: number): string {
    return new Intl.NumberFormat('default', {
        minimumFractionDigits: 0,
        maximumFractionDigits: 2,
    }).format(num);
}

/**
 * Formats the dice into a human readable form.
 */
export function diceFormatter(dice?: Dice): string {
    if (!dice || !dice.dices) {
        return "";
    }

    return dice.dices.map(d => {
        if (d.numberOfThrows === 1) {
            return "D" + d.dice;
        } else {
            return d.numberOfThrows + " D" + d.dice;
        }
    }).join(" + ");
}

/**
 * Formats the resource usage into a human readable form.
 */
export function resourceFormatter(usage: IResourceUsage): string {
    if (!usage || usage.resource === null) {
        return "";
    }
    return usage.amount + " " + usage.resource.name;
}