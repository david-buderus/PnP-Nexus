import {API_CONFIGURATION, SomeItem} from '../Constants';
import {Inventory, InventoryServiceApi} from '../../api';

const INVENTORY_SERVICE = new InventoryServiceApi(API_CONFIGURATION);

/** Adds an item to the inventory */
export async function addItemToInventory(inventory: Inventory, item: SomeItem, amount: number): Promise<Inventory> {
    return INVENTORY_SERVICE.add({
        inventory: inventory,
        item: item,
        amount: amount,
    }).then(response => response.data);
}
