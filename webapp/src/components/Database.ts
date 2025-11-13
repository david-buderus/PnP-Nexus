import {AxiosRequestConfig, AxiosResponse} from 'axios';
import {useEffect, useMemo, useState} from 'react';
import {useUniverseContext} from './PageBase';
import {
    Armor,
    CharacterResourceUsage,
    CraftingRecipe,
    CraftingRecipeServiceApi,
    Item,
    ItemServiceApi,
    ItemUsage,
    Jewellery,
    Material,
    MaterialServiceApi,
    MaterialUsage,
    Nation,
    NationServiceApi,
    PnPCharacterDto,
    PnPCharacterServiceApi,
    PnPCharacterSheet,
    PnPCharacterSheetServiceApi,
    PrimaryAttribute,
    PrimaryAttributeServiceApi,
    SecondaryAttribute,
    SecondaryAttributeDTO,
    SecondaryAttributeServiceApi,
    Shield,
    SimpleSecondaryAttributeServiceApi,
    Species,
    SpeciesServiceApi,
    Spell,
    SpellServiceApi,
    TagServiceApi,
    Talent,
    TalentServiceApi,
    Upgrade,
    UpgradeRecipe,
    UpgradeRecipeServiceApi,
    UpgradeServiceApi,
    Weapon
} from '../api';
import {API_CONFIGURATION, SomeItem} from './Constants';
import {handleNetworkErrors} from './utils/ErrorUtils';

const ITEM_API = new ItemServiceApi(API_CONFIGURATION);
const MATERIAL_API = new MaterialServiceApi(API_CONFIGURATION);
const PRIMARY_ATTRIBUTE_API = new PrimaryAttributeServiceApi(API_CONFIGURATION);
const SIMPLE_SECONDARY_ATTRIBUTE_API = new SimpleSecondaryAttributeServiceApi(API_CONFIGURATION);
const SECONDARY_ATTRIBUTE_API = new SecondaryAttributeServiceApi(API_CONFIGURATION);
const TAG_API = new TagServiceApi(API_CONFIGURATION);
const UPGRADE_API = new UpgradeServiceApi(API_CONFIGURATION);
const CRAFTING_RECIPE_API = new CraftingRecipeServiceApi(API_CONFIGURATION);
const UPGRADE_RECIPE_API = new UpgradeRecipeServiceApi(API_CONFIGURATION);
const SPELL_API = new SpellServiceApi(API_CONFIGURATION);
const TALENT_API = new TalentServiceApi(API_CONFIGURATION);
const SPECIES_API = new SpeciesServiceApi(API_CONFIGURATION);
const NATION_API = new NationServiceApi(API_CONFIGURATION);
const CHARACTER_API = new PnPCharacterServiceApi(API_CONFIGURATION);
const SHEET_API = new PnPCharacterSheetServiceApi(API_CONFIGURATION);

/** Super type of all possible resource usages */
export type IResourceUsage = ItemUsage | MaterialUsage | CharacterResourceUsage;

/** Super type of all possible resources */
export type IResource = SomeItem | Material | SecondaryAttributeDTO;

/**
 * Fetches all objects for the given fetch method.
 * Returns the data, a refresh callback and if the data is currenlty loading.
 */
export function fetchAll<O>(fetch: ((universe: string, options?: AxiosRequestConfig) => Promise<AxiosResponse<O[]>>)): [O[], () => void, boolean] {
    const {activeUniverse} = useUniverseContext();

    const [objects, setObjects] = useState<O[]>([]);
    const [loading, setLoading] = useState<boolean>(false);

    function refresh() {
        if (!activeUniverse) {
            return;
        }
        setLoading(true);
        fetch(activeUniverse.name).then(response => {
            setLoading(false);
            setObjects(response.data);
        }).catch(err => {
            setLoading(false);
            handleNetworkErrors()(err);
        });
    }

    useEffect(refresh, [activeUniverse]);

    return [objects, refresh, loading];
}

/**
 * Fetches all items.
 */
export function fetchAllItems(): [Item[], () => void, boolean] {
    return fetchAll(universe => ITEM_API.getAllItems(universe));
}

/**
 * Fetches all weapons.
 */
export function fetchAllWeapons(): [Weapon[], () => void, boolean] {
    return fetchAll(universe => ITEM_API.getAllWeapons(universe));
}

/**
 * Fetches all shields.
 */
export function fetchAllShields(): [Shield[], () => void, boolean] {
    return fetchAll(universe => ITEM_API.getAllShields(universe));
}

/**
 * Fetches all armor.
 */
export function fetchAllArmor(): [Armor[], () => void, boolean] {
    return fetchAll(universe => ITEM_API.getAllArmor(universe));
}

/**
 * Fetches all jewellery.
 */
export function fetchAllJewllery(): [Jewellery[], () => void, boolean] {
    return fetchAll(universe => ITEM_API.getAllJewellery(universe));
}

/**
 * Fetches all materials.
 */
export function fetchAllMaterials(): [Material[], () => void, boolean] {
    return fetchAll(universe => MATERIAL_API.getAllMaterials(universe));
}

/**
 * Fetches all primary attributes.
 */
export function fetchAllPrimaryAttributes(): [PrimaryAttribute[], () => void, boolean] {
    return fetchAll(universe => PRIMARY_ATTRIBUTE_API.getAllPrimaryAttributes(universe));
}

/**
 * Fetches all secondary attributes.
 */
export function fetchAllSimpleSecondaryAttributes(): [SecondaryAttributeDTO[], () => void, boolean] {
    return fetchAll(universe => SIMPLE_SECONDARY_ATTRIBUTE_API.getAllSimpleSecondaryAttributes(universe));
}

/**
 * Fetches all secondary attributes.
 */
export function fetchAllSecondaryAttributes(): [SecondaryAttribute[], () => void, boolean] {
    return fetchAll(universe => SECONDARY_ATTRIBUTE_API.getAllSecondaryAttributes(universe));
}

/**
 * Fetches all known tags.
 */
export function fetchAllTags(): [string[], () => void, boolean] {
    return fetchAll(universe => TAG_API.getAllTags(universe));
}

/**
 * Fetches all upgrades.
 */
export function fetchAllUpgrades(): [Upgrade[], () => void, boolean] {
    return fetchAll(universe => UPGRADE_API.getAllUpgrades(universe));
}

/**
 * Fetches all variables supported by the secondary attributes.
 */
export function fetchSupportedSecondaryAttributeVariables(): [string[], () => void, boolean] {
    return fetchAll(universe => SIMPLE_SECONDARY_ATTRIBUTE_API.getAllSupportedVariables(universe));
}

/**
 * Fetches all crafting recipes.
 */
export function fetchAllCraftingRecipes(): [CraftingRecipe[], () => void, boolean] {
    return fetchAll(universe => CRAFTING_RECIPE_API.getAllCraftingRecipes(universe));
}

/**
 * Fetches all upgrade recipes.
 */
export function fetchAllUpgradeRecipes(): [UpgradeRecipe[], () => void, boolean] {
    return fetchAll(universe => UPGRADE_RECIPE_API.getAllUpgradeRecipes(universe));
}

/**
 * Fetches all spells.
 */
export function fetchAllTalents(): [Talent[], () => void, boolean] {
    return fetchAll(universe => TALENT_API.getAllTalents(universe));
}

/**
 * Fetches all spells.
 */
export function fetchAllSpells(): [Spell[], () => void, boolean] {
    return fetchAll(universe => SPELL_API.getAllSpells(universe));
}

/**
 * Fetches all species.
 */
export function fetchAllSpecies(): [Species[], () => void, boolean] {
    return fetchAll(universe => SPECIES_API.getAllSpeciess(universe));
}

/**
 * Fetches all species.
 */
export function fetchAllNations(): [Nation[], () => void, boolean] {
    return fetchAll(universe => NATION_API.getAllNations(universe));
}

/**
 * Fetches all characters.
 */
export function fetchAllCharacters(): [PnPCharacterDto[], () => void, boolean] {
    return fetchAll(universe => CHARACTER_API.getAllCharacters(universe));
}

/**
 * Fetches all character sheets.
 */
export function fetchAllCharacterSheets(): [PnPCharacterSheet[], () => void, boolean] {
    return fetchAll(universe => SHEET_API.getAllPnPCharacterSheets(universe));
}

/** Fetches all possible resource for the given universe. */
export function fetchAllResources(): [IResource[], () => void, boolean] {
    const [items, refreshItems, loadingItems] = fetchAllItems();
    const [materials, refreshMaterials, loadingMaterials] = fetchAllMaterials();
    const [attributes, refreshAttributes, loadingAttributes] = fetchAllSimpleSecondaryAttributes();

    const resources: IResource[] = useMemo(() => {
        return [].concat(items).concat(materials).concat(attributes.filter(attribute => attribute.consumable));
    }, [items, materials, attributes]);

    const loading: boolean = useMemo(() => {
        return loadingItems || loadingMaterials || loadingAttributes;
    }, [loadingItems, loadingMaterials, loadingAttributes]);

    return [resources, () => {
        refreshItems();
        refreshMaterials();
        refreshAttributes();
    }, loading];
} 