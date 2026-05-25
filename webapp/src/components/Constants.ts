import {
    Armor,
    ArmorEquipment,
    EquipmentItemEffect,
    Item,
    ItemEffect,
    ItemStack,
    Jewellery,
    JewelleryEquipment,
    Shield,
    ShieldEquipment,
    Weapon,
    WeaponEquipment
} from '../api/model';

/** The possible identifier for each item class */
export type ItemClass = 'Item' | 'Weapon' | 'Shield' | 'Armor' | 'Jewellery';

/** All possible item classes */
export type SomeItem = Item | Weapon | Shield | Armor | Jewellery;

/** All possible item stack classes */
export type SomeItemStack = ArmorEquipment | JewelleryEquipment | ItemStack | ShieldEquipment | WeaponEquipment;

/** All item effect classes */
export type SomeItemEffect = ItemEffect | EquipmentItemEffect;

/** All equipment classes */
export type SomeEquipment = ArmorEquipment | JewelleryEquipment | ShieldEquipment | WeaponEquipment;
