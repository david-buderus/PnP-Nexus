import {API_CONFIGURATION, SomeEquipment, SomeItem} from '../Constants';
import {Inventory, InventoryServiceApi, Upgrade, UpgradeItemServiceApi} from '../../api';

const INVENTORY_SERVICE = new InventoryServiceApi(API_CONFIGURATION);
const UPGRADE_SERVICE = new UpgradeItemServiceApi(API_CONFIGURATION);

/** Adds an item to the inventory */
export async function addItemToInventory(inventory: Inventory, item: SomeItem, amount: number): Promise<Inventory> {
    return INVENTORY_SERVICE.add({
        inventory: inventory,
        item: item,
        amount: amount,
    }).then(response => response.data);
}

/** Removes an item from the inventory */
export async function removeItemFromInventory(inventory: Inventory, item: SomeItem, amount: number): Promise<Inventory> {
    return INVENTORY_SERVICE.remove({
        inventory: inventory,
        item: item,
        amount: amount,
    }).then(response => response.data);
}

/**
 * Upgrades the given item and returns it.
 */
export async function upgradeEquipment<T extends SomeEquipment>(equipment: T, upgrade: Upgrade): Promise<T> {
    return UPGRADE_SERVICE.addUpgrade({
        equipment: equipment,
        upgrade: upgrade
    }).then(response => response.data as T);
}

/**
 * Removes the upgrades from the given item and returns it.
 */
export async function removeUpgradeFromEquipment<T extends SomeEquipment>(equipment: T, upgrade: Upgrade): Promise<T> {
    return UPGRADE_SERVICE.removeUpgrade({
        equipment: equipment,
        upgrade: upgrade
    }).then(response => response.data as T);
}