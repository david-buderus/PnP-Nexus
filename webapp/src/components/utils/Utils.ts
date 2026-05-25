import {useMemo} from 'react';
import {CurrencySettings} from '../../api/model';

/** A type with an ID */
type WithId = {
    /** The id of the type */
    id?: string
}

/**
 * Converts a list to a map based on the IDs of the objets.
 */
export function toIdMap<T extends WithId>(list: T[]): Record<string, T> {
    return useMemo(() => list.reduce((map, attribute) => {
        map[attribute.id] = attribute;
        return map;
    }, {} as Record<string, T>), [list]);
}

/**
 * Resizes the array to the given length.
 */
export function resizeArray<T>(list: T[], size: number): T[] {
    const result = list.slice(0, size);
    while (result.length < size) {
        result.push(null);
    }
    return result;
}

/**
 * Splits the currency into its coins.
 */
export function splitCurrency(currencySettings: CurrencySettings, amount: number): number[] {
    if (!currencySettings) {
        return [];
    }
    const result = new Array<number>(currencySettings.calculationEntries.length + 1).fill(0);

    if (Number.isNaN(amount)) {
        return result;
    }

    if (amount <= 0) {
        return result;
    }
    if (currencySettings.calculationEntries.length === 0) {
        result[0] = amount;
        return result;
    }

    const calculationSteps: number[] = [1];

    for (const entry of currencySettings.calculationEntries) {
        if (entry.factor <= 0) {
            continue;
        }
        calculationSteps.unshift(calculationSteps[0] * (entry.factor ?? 1));
    }

    let remainingAmount = amount;

    for (let i = 0; i < calculationSteps.length; i++) {
        const factor = calculationSteps[i];
        const coins = Math.floor(remainingAmount / factor);
        if (coins > 0) {
            result[result.length - 1 - i] = coins;
        }
        remainingAmount %= factor;

        if (remainingAmount <= 0) {
            break;
        }
    }

    return result;
}