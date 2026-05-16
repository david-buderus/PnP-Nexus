import {CurrencySettings, Dice, ERarity, SpellCast, TagCast, TalentCast} from '../../api';
import {IResourceUsage} from '../Database';
import {TFunction} from 'i18next';
import {splitCurrency} from './Utils';

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
        return '';
    }

    if (amount <= 0) {
        return '0' + currencySettings.baseCurrencyShortForm;
    }

    const coins = splitCurrency(currencySettings, amount);
    let result = '';

    for (let i = 0; i < coins.length; i++) {
        const coin = coins[i];
        if (coin <= 0) {
            continue;
        }
        if (i === 0) {
            result = coin + currencySettings.baseCurrencyShortForm + ' ' + result;
        } else {
            const entry = currencySettings.calculationEntries[i - 1];
            result = coin + entry.currencyShortForm + ' ' + result;
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
        return '';
    }

    return dice.dices.map(d => {
        if (d.numberOfThrows === 1) {
            return 'D' + d.dice;
        } else {
            return d.numberOfThrows + ' D' + d.dice;
        }
    }).join(' + ');
}

/**
 * Formats the resource usage into a human-readable form.
 */
export function resourceFormatter(usage: IResourceUsage): string {
    if (!usage || usage.resource === null) {
        return '';
    }
    return usage.amount + ' ' + usage.resource.name;
}

/**
 * Formats the spell cast into a human-readable form.
 */
export function spellCastFormatter(cast: SpellCast, t: TFunction<'translation', undefined>): string {
    if (!cast) {
        return '';
    }
    if (cast['@type'] === 'TalentCast') {
        return (cast as TalentCast).talents.map(o => o?.name ?? '-').join(', ');
    } else {
        return (cast as TagCast).tagRequirement.tagRequirements.map(tags => tags.join(', ')).join(' ' + t('or') + ' ');
    }
}

/**
 * Returns the color to display for the given rarity.
 */
export function getRarityColor(rarity: ERarity) {
    switch (rarity) {
        case ERarity.Common:
            return 'gray';
        case ERarity.Uncommon:
            return 'green';
        case ERarity.Rare:
            return 'blue';
        case ERarity.Epic:
            return 'purple';
        case ERarity.Legendary:
            return 'orange';
        case ERarity.Godlike:
            return 'red';
        default:
            return 'white';
    }
}