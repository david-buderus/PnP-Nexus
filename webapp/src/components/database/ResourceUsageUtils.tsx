import { useEffect, useMemo, useState } from "react";
import { CharacterResourceUsage, Item, ItemServiceApi, ItemUsage, Material, MaterialServiceApi, MaterialUsage, SecondaryAttribute, SecondaryAttributeServiceApi, Universe } from "../../api";
import { API_CONFIGURATION, SomeItem } from "../Constants";

/** Super type of all possible resource usages */
export type IResourceUsage = ItemUsage | MaterialUsage | CharacterResourceUsage;

const ITEM_API = new ItemServiceApi(API_CONFIGURATION);
const MATERIAL_API = new MaterialServiceApi(API_CONFIGURATION);
const ATTRIBUTE_API = new SecondaryAttributeServiceApi(API_CONFIGURATION);

/** Adds the necessary types to the usage */
export function addTypeAnnotationToUsage<E extends IResourceUsage>(usage: E): E {
    if (usage === undefined || usage === null) {
        return usage;
    }
    if (usage.resource === undefined || usage.resource === null) {
        return {
            ...usage,
            "@type": "ItemUsage"
        };
    }
    if ((usage.resource as Item).type !== undefined) {
        return {
            ...usage,
            "@type": "ItemUsage"
        };
    }
    if ((usage.resource as Material).items !== undefined) {
        return {
            ...usage,
            "@type": "MaterialUsage"
        };
    }
    if ((usage.resource as SecondaryAttribute).consumable !== undefined) {
        return {
            ...usage,
            "@type": "CharacterResourceUsage"
        };
    }
    return usage;
}

/** Converts a recipe entry to a human-readable string */
export function resourceUsageToString(usage: IResourceUsage): string {
    if (!usage) {
        return "";
    }
    return usage.amount + " " + usage.resource.name;
}

/** Fetches all possible resource for the given universe. Automatically refreshed if the active universe changes. */
export function fetchAllResources(activeUniverse: Universe): IResourceUsage[] {
    const [items, setItems] = useState<SomeItem[]>([]);
    const [materials, setMaterials] = useState<Material[]>([]);
    const [secondaryAttributes, setSecondaryAttributes] = useState<SecondaryAttribute[]>([]);

    const resources: IResourceUsage[] = useMemo(() => {
        return [].concat(items).concat(materials).concat(secondaryAttributes);
    }, [items, materials, secondaryAttributes]);

    useEffect(() => {
        if (!activeUniverse) {
            return;
        }
        Promise.all([
            ITEM_API.getAllItems(activeUniverse.name),
            MATERIAL_API.getAllMaterials(activeUniverse.name),
            ATTRIBUTE_API.getAllSecondaryAttributes(activeUniverse.name)
        ]).then(([itemReponse, materialsResponse, attributeResponse]) => {
            setItems(itemReponse.data);
            setMaterials(materialsResponse.data);
            setSecondaryAttributes(attributeResponse.data.filter(attribute => attribute.consumable));
        });
    }, [activeUniverse]);

    return resources;
} 