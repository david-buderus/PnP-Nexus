import {ItemClass} from '../Constants';
import {EItemEquipmentManipulator, EUpgradeRestriction} from '../../api/model';

/** Get allowed restrictions for the given item class */
export function getPossibleUpgradeRestriction(itemClass: ItemClass): EUpgradeRestriction[] {
    switch (itemClass) {
        case 'Item':
            return [EUpgradeRestriction.ITEM];
        case 'Weapon':
            return [EUpgradeRestriction.WEAPON, EUpgradeRestriction.HANDHELD, EUpgradeRestriction.EQUIPMENT, EUpgradeRestriction.ITEM];
        case 'Shield':
            return [EUpgradeRestriction.SHIELD, EUpgradeRestriction.DEFENSIVE_ITEM, EUpgradeRestriction.HANDHELD, EUpgradeRestriction.EQUIPMENT, EUpgradeRestriction.ITEM];
        case 'Armor':
            return [EUpgradeRestriction.ARMOR, EUpgradeRestriction.DEFENSIVE_ITEM, EUpgradeRestriction.EQUIPMENT, EUpgradeRestriction.ITEM];
        case 'Jewellery':
            return [EUpgradeRestriction.EQUIPMENT, EUpgradeRestriction.ITEM];
        default:
            return [];
    }
}

/**
 * Returns all {@link EItemEquipmentManipulator} supported by the given {@link EUpgradeRestriction}.
 */
export function getPossibleUpgradeManipulators(restrictions: EUpgradeRestriction[]): Set<EItemEquipmentManipulator> {
    const manipulators = new Set<EItemEquipmentManipulator>();
    manipulators.add(EItemEquipmentManipulator.SLOTS);

    if (restrictions.includes(EUpgradeRestriction.WEAPON)) {
        manipulators.add(EItemEquipmentManipulator.DAMAGE);
        manipulators.add(EItemEquipmentManipulator.HIT);
        manipulators.add(EItemEquipmentManipulator.INITIATIVE);
    }
    if (restrictions.includes(EUpgradeRestriction.DEFENSIVE_ITEM)) {
        manipulators.add(EItemEquipmentManipulator.ARMOR);
        manipulators.add(EItemEquipmentManipulator.WEIGHT);
        manipulators.add(EItemEquipmentManipulator.PROTECTION);
    }
    if (restrictions.includes(EUpgradeRestriction.SHIELD)) {
        manipulators.add(EItemEquipmentManipulator.ARMOR);
        manipulators.add(EItemEquipmentManipulator.WEIGHT);
        manipulators.add(EItemEquipmentManipulator.PROTECTION);
        manipulators.add(EItemEquipmentManipulator.HIT);
        manipulators.add(EItemEquipmentManipulator.INITIATIVE);
    }
    if (restrictions.includes(EUpgradeRestriction.HANDHELD)) {
        manipulators.add(EItemEquipmentManipulator.HIT);
        manipulators.add(EItemEquipmentManipulator.INITIATIVE);
    }
    if (restrictions.includes(EUpgradeRestriction.ARMOR)) {
        manipulators.add(EItemEquipmentManipulator.ARMOR);
        manipulators.add(EItemEquipmentManipulator.WEIGHT);
        manipulators.add(EItemEquipmentManipulator.PROTECTION);
    }

    return manipulators;
}