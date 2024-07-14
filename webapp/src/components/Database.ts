import { AxiosRequestConfig, AxiosResponse } from "axios";
import { useState, useEffect } from "react";
import { getUniverseContext } from "./PageBase";
import { Item, ItemServiceApi, ItemType, ItemTypeServiceApi, PrimaryAttribute, PrimaryAttributeServiceApi } from "../api";
import { API_CONFIGURATION } from "./Constants";

const ITEM_API = new ItemServiceApi(API_CONFIGURATION);
const ITEM_TYPE_API = new ItemTypeServiceApi(API_CONFIGURATION);
const PRIMARY_ATTRIBUTE_API = new PrimaryAttributeServiceApi(API_CONFIGURATION);

/**
 * Fetches all objects for the given fetch method.
 * Returns the data and a refresh callback.
 */
export function fetchAll<O>(fetch: ((universe: string, options?: AxiosRequestConfig) => Promise<AxiosResponse<O[], any>>)): [O[], () => void] {
    const { activeUniverse } = getUniverseContext();

    const [objects, setObjects] = useState<O[]>([]);

    function refresh() {
        if (!activeUniverse) {
            return;
        }
        fetch(activeUniverse.name).then(response => setObjects(response.data));
    }
    useEffect(refresh, [activeUniverse]);

    return [objects, refresh];
}

/**
 * Fetches all items.
 */
export function fetchAllItems(): [Item[], () => void] {
    return fetchAll(universe => ITEM_API.getAllItems(universe));
}

/**
 * Fetches all item types.
 */
export function fetchAllItemTypes(): [ItemType[], () => void] {
    return fetchAll(universe => ITEM_TYPE_API.getAllItemTypes(universe));
}

/**
 * Fetches all item types.
 */
export function fetchAllPrimaryAttributes(): [PrimaryAttribute[], () => void] {
    return fetchAll(universe => PRIMARY_ATTRIBUTE_API.getAllPrimaryAttributes(universe));
}
