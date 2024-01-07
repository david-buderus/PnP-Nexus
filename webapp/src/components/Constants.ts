import { Item, Weapon, Shield, Armor, Jewellery } from "../api";

/** The possible identifier for each item class */
export type ItemClass = "item" | "weapon" | "shield" | "armor" | "jewellery";

/** All possible item classes */
export type SomeItem = Item | Weapon | Shield | Armor | Jewellery