import {ItemClass} from '../Constants';
import {EItemEquipmentManipulator, EUpgradeRestriction} from '../../api';

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

/**
 * Returns all {@link EItemEquipmentManipulator} supported by the given {@link EUpgradeRestriction}.
 */
export function getPossibleUpgradeManipulators(restrictions: EUpgradeRestriction[]): Set<EItemEquipmentManipulator> {
    const manipulators = new Set<EItemEquipmentManipulator>();
    manipulators.add(EItemEquipmentManipulator.Slots);

    if (restrictions.includes(EUpgradeRestriction.Weapon)) {
        manipulators.add(EItemEquipmentManipulator.Damage);
        manipulators.add(EItemEquipmentManipulator.Hit);
        manipulators.add(EItemEquipmentManipulator.Initiative);
    }
    if (restrictions.includes(EUpgradeRestriction.DefensiveItem)) {
        manipulators.add(EItemEquipmentManipulator.Armor);
        manipulators.add(EItemEquipmentManipulator.Weight);
        manipulators.add(EItemEquipmentManipulator.Protection);
    }
    if (restrictions.includes(EUpgradeRestriction.Shield)) {
        manipulators.add(EItemEquipmentManipulator.Armor);
        manipulators.add(EItemEquipmentManipulator.Weight);
        manipulators.add(EItemEquipmentManipulator.Protection);
        manipulators.add(EItemEquipmentManipulator.Hit);
        manipulators.add(EItemEquipmentManipulator.Initiative);
    }
    if (restrictions.includes(EUpgradeRestriction.Handheld)) {
        manipulators.add(EItemEquipmentManipulator.Hit);
        manipulators.add(EItemEquipmentManipulator.Initiative);
    }
    if (restrictions.includes(EUpgradeRestriction.Armor)) {
        manipulators.add(EItemEquipmentManipulator.Armor);
        manipulators.add(EItemEquipmentManipulator.Weight);
        manipulators.add(EItemEquipmentManipulator.Protection);
    }

    return manipulators;
}