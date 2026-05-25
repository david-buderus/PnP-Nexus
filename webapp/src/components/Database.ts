import {useUniverseContext} from './PageBase';
import {
    Armor,
    CharacterResourceUsage,
    CraftingRecipe,
    Item,
    ItemUsage,
    Jewellery,
    Material,
    MaterialUsage,
    Nation,
    PnPCharacterDTO,
    PnPCharacterSheet,
    PrimaryAttribute,
    SecondaryAttribute,
    SecondaryAttributeDTO,
    Shield,
    Species,
    Spell,
    Talent,
    Upgrade,
    UpgradeRecipe,
    Weapon
} from '../api/model';
import {SomeItem} from './Constants';
import {
    useGetAllArmor,
    useGetAllItems,
    useGetAllJewellery,
    useGetAllShields,
    useGetAllWeapons
} from '../api/item-service/item-service';
import {useGetAllMaterials} from '../api/material-service/material-service';
import {useGetAllPrimaryAttributes} from '../api/primary-attribute-service/primary-attribute-service';
import {
    useGetAllSimpleSecondaryAttributes,
    useGetAllSupportedVariables
} from '../api/simple-secondary-attribute-service/simple-secondary-attribute-service';
import {useGetAllSecondaryAttributes} from '../api/secondary-attribute-service/secondary-attribute-service';
import {useGetAllTags} from '../api/tag-service/tag-service';
import {useGetAllUpgrades} from '../api/upgrade-service/upgrade-service';
import {useGetAllCraftingRecipes} from '../api/crafting-recipe-service/crafting-recipe-service';
import {useGetAllUpgradeRecipes} from '../api/upgrade-recipe-service/upgrade-recipe-service';
import {useGetAllTalents} from '../api/talent-service/talent-service';
import {useGetAllSpells} from '../api/spell-service/spell-service';
import {useGetAllSpeciess} from '../api/species-service/species-service';
import {useGetAllNations} from '../api/nation-service/nation-service';
import {useGetAllCharacters} from '../api/pn-p-character-service/pn-p-character-service';
import {useGetAllPnPCharacterSheets} from '../api/pn-p-character-sheet-service/pn-p-character-sheet-service';

/** Super type of all possible resource usages */
export type IResourceUsage = ItemUsage | MaterialUsage | CharacterResourceUsage;

/** Super type of all possible resources */
export type IResource = SomeItem | Material | SecondaryAttributeDTO;

type GetAllQueryHook<T> = (
    universeId: string | undefined,
    params?: any,
    options?: { query?: { enabled?: boolean } }
) => {
    data?: { data?: T[] };
    refetch: () => void;
    isLoading: boolean;
};

type GetAllUnfilteredQueryHook<T> = (
    universeId: string | undefined,
    options?: { query?: { enabled?: boolean } }
) => {
    data?: { data?: T[] };
    refetch: () => void;
    isLoading: boolean;
};

/**
 * Fetches all objects for the given fetch method.
 * Returns the data, a refresh callback and if the data is currenlty loading.
 */
function fetchAll<O>(useQueryHook: GetAllQueryHook<O>): [O[], () => void, boolean] {
    const {activeUniverse} = useUniverseContext();

    const {data, refetch, isLoading} = useQueryHook(
        activeUniverse?.id,
        {},
        {query: {enabled: Boolean(activeUniverse?.id)}}
    );

    return [data?.data ?? [], refetch, isLoading];
}

/**
 * Fetches all objects for the given fetch method.
 * Returns the data, a refresh callback and if the data is currenlty loading.
 */
function fetchAllUnfiltered<O>(useQueryHook: GetAllUnfilteredQueryHook<O>): [O[], () => void, boolean] {
    const {activeUniverse} = useUniverseContext();

    const {data, refetch, isLoading} = useQueryHook(
        activeUniverse?.id,
        {query: {enabled: Boolean(activeUniverse?.id)}}
    );

    return [data?.data ?? [], refetch, isLoading];
}

/**
 * Fetches all items.
 */
export function fetchAllItems(): [Item[], () => void, boolean] {
    return fetchAll(useGetAllItems);
}

/**
 * Fetches all weapons.
 */
export function fetchAllWeapons(): [Weapon[], () => void, boolean] {
    return fetchAllUnfiltered(useGetAllWeapons);
}

/**
 * Fetches all shields.
 */
export function fetchAllShields(): [Shield[], () => void, boolean] {
    return fetchAllUnfiltered(useGetAllShields);
}

/**
 * Fetches all armor.
 */
export function fetchAllArmor(): [Armor[], () => void, boolean] {
    return fetchAllUnfiltered(useGetAllArmor);
}

/**
 * Fetches all jewellery.
 */
export function fetchAllJewllery(): [Jewellery[], () => void, boolean] {
    return fetchAllUnfiltered(useGetAllJewellery);
}

/**
 * Fetches all materials.
 */
export function fetchAllMaterials(): [Material[], () => void, boolean] {
    return fetchAll(useGetAllMaterials);
}

/**
 * Fetches all primary attributes.
 */
export function fetchAllPrimaryAttributes(): [PrimaryAttribute[], () => void, boolean] {
    return fetchAll(useGetAllPrimaryAttributes);
}

/**
 * Fetches all secondary attributes.
 */
export function fetchAllSimpleSecondaryAttributes(): [SecondaryAttributeDTO[], () => void, boolean] {
    return fetchAll(useGetAllSimpleSecondaryAttributes);
}

/**
 * Fetches all secondary attributes.
 */
export function fetchAllSecondaryAttributes(): [SecondaryAttribute[], () => void, boolean] {
    return fetchAll(useGetAllSecondaryAttributes);
}

/**
 * Fetches all known tags.
 */
export function fetchAllTags(): [string[], () => void, boolean] {
    return fetchAllUnfiltered(useGetAllTags);
}

/**
 * Fetches all upgrades.
 */
export function fetchAllUpgrades(): [Upgrade[], () => void, boolean] {
    return fetchAll(useGetAllUpgrades);
}

/**
 * Fetches all variables supported by the secondary attributes.
 */
export function fetchSupportedSecondaryAttributeVariables(): [string[], () => void, boolean] {
    return fetchAllUnfiltered(useGetAllSupportedVariables);
}

/**
 * Fetches all crafting recipes.
 */
export function fetchAllCraftingRecipes(): [CraftingRecipe[], () => void, boolean] {
    return fetchAll(useGetAllCraftingRecipes);
}

/**
 * Fetches all upgrade recipes.
 */
export function fetchAllUpgradeRecipes(): [UpgradeRecipe[], () => void, boolean] {
    return fetchAll(useGetAllUpgradeRecipes);
}

/**
 * Fetches all spells.
 */
export function fetchAllTalents(): [Talent[], () => void, boolean] {
    return fetchAll(useGetAllTalents);
}

/**
 * Fetches all spells.
 */
export function fetchAllSpells(): [Spell[], () => void, boolean] {
    return fetchAll(useGetAllSpells);
}

/**
 * Fetches all species.
 */
export function fetchAllSpecies(): [Species[], () => void, boolean] {
    return fetchAll(useGetAllSpeciess);
}

/**
 * Fetches all species.
 */
export function fetchAllNations(): [Nation[], () => void, boolean] {
    return fetchAll(useGetAllNations);
}

/**
 * Fetches all characters.
 */
export function fetchAllCharacters(): [PnPCharacterDTO[], () => void, boolean] {
    return fetchAllUnfiltered(useGetAllCharacters);
}

/**
 * Fetches all character sheets.
 */
export function fetchAllCharacterSheets(): [PnPCharacterSheet[], () => void, boolean] {
    return fetchAll(useGetAllPnPCharacterSheets);
}