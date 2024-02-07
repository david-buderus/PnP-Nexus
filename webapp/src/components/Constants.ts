import { Item, Weapon, Shield, Armor, Jewellery, Configuration } from "../api";

/** The possible identifier for each item class */
export type ItemClass = "Item" | "Weapon" | "Shield" | "Armor" | "Jewellery";

/** All possible item classes */
export type SomeItem = Item | Weapon | Shield | Armor | Jewellery;

export const ApiConfiguration = new Configuration({
    basePath: window.location.origin
}); 