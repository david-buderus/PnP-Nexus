import { CurrencySettings, Dice, SingleDice, Talent, Universe } from "../api";

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
export function currencyToHumanReadable(currencySettings: CurrencySettings, amount: number): string {
    if (Number.isNaN(amount)) {
        return "";
    }
    if (currencySettings === null) {
        return amount.toString();
    }

    if (amount <= 0) {
        return "0" + currencySettings.baseCurrencyShortForm;
    }
    if (currencySettings.calculationEntries.length === 0) {
        return amount + currencySettings.baseCurrencyShortForm;
    }

    const calcuationSteps: CurrencyCalculationStep[] = [{ shortForm: currencySettings.baseCurrencyShortForm, factor: 1 }];

    for (const entry of currencySettings.calculationEntries) {
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
 * Returns the probability for a throw with at least the given values.
 */
export function probabilityForSuccesfulThrows(a1: number, a2: number, a3: number, succesfulThrows: number = 2) {
    const p1 = prohabilites(a1);
    const p2 = prohabilites(a2);
    const p3 = prohabilites(a3);

    let chance = 0;
    for (let x1 = -1; x1 < 3; x1++) {
        for (let x2 = -1; x2 < 3; x2++) {
            for (let x3 = -1; x3 < 3; x3++) {
                if (x1 + x2 + x3 >= succesfulThrows) {
                    chance += p1[x1] * p2[x2] * p3[x3];
                }
            }
        }
    }
    return chance;
}

function prohabilites(x: number): {
    "-1": number;
    "0": number;
    "1": number;
    "2": number;
} {
    if (x < 1) {
        return {
            "-1": 1 / 20,
            "0": 18 / 20,
            "1": 1 / 20,
            "2": 0
        };
    }
    if (x >= 20) {
        return {
            "-1": 1 / 20,
            "0": 0,
            "1": 18 / 20,
            "2": 1 / 20
        };
    }
    return {
        "-1": 1 / 20,
        "0": (19 - x) / 20,
        "1": (x - 1) / 20,
        "2": 1 / 20
    };
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