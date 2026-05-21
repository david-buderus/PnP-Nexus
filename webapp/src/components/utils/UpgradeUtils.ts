import {ItemClass} from '../Constants';
import {EUpgradeRestriction} from '../../api';

/** Get allowed restrictions for the given item class */
export function getPossibleUpgradeRestriction(itemClass: ItemClass): EUpgradeRestriction[] {
    switch (itemClass) {
        case 'Item':
            return [EUpgradeRestriction.Item];
        case 'Weapon':
            return [EUpgradeRestriction.Weapon, EUpgradeRestriction.Handheld, EUpgradeRestriction.Equipment, EUpgradeRestriction.Item];
        case 'Shield':
            return [EUpgradeRestriction.Shield, EUpgradeRestriction.DefensiveItem, EUpgradeRestriction.Handheld, EUpgradeRestriction.Equipment, EUpgradeRestriction.Item];
        case 'Armor':
            return [EUpgradeRestriction.Armor, EUpgradeRestriction.DefensiveItem, EUpgradeRestriction.Equipment, EUpgradeRestriction.Item];
        case 'Jewellery':
            return [EUpgradeRestriction.Equipment, EUpgradeRestriction.Item];
        default:
            return [];
    }
}