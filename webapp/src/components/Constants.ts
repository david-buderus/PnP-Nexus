import {
    Armor,
    ArmorEquipment,
    Configuration,
    Equipment,
    EquipmentItemEffect,
    Item,
    ItemEffect,
    ItemStackItem,
    Jewellery,
    Shield,
    ShieldEquipment,
    Weapon,
    WeaponEquipment
} from '../api';

/** The possible identifier for each item class */
export type ItemClass = 'Item' | 'Weapon' | 'Shield' | 'Armor' | 'Jewellery';

/** All possible item classes */
export type SomeItem = Item | Weapon | Shield | Armor | Jewellery;

/** All possible item stack classes */
export type ItemStack = ArmorEquipment | Equipment | ItemStackItem | ShieldEquipment | WeaponEquipment;

/** All item effect classes */
export type SomeItemEffect = ItemEffect | EquipmentItemEffect;

/** All equipment classes */
export type SomeEquipment = ArmorEquipment | Equipment | ShieldEquipment | WeaponEquipment;

/** The api configuration which should be used by all APIs */
export const API_CONFIGURATION = new Configuration({
    basePath: window.location.origin
});
