import { createTheme } from "@mui/material";
import { Item, Weapon, Shield, Armor, Jewellery, Configuration } from "../api";

/** The possible identifier for each item class */
export type ItemClass = "Item" | "Weapon" | "Shield" | "Armor" | "Jewellery";

/** All possible item classes */
export type SomeItem = Item | Weapon | Shield | Armor | Jewellery;

/** The api configration which should be used by all APIs */
export const API_CONFIGURATION = new Configuration({
    basePath: window.location.origin
});

/** The base theme of the webapp */
export const THEME = createTheme({
    palette: {
        primary: {
            main: '#4a148c',
        },
        secondary: {
            main: '#4a148c',
        },
    },
});