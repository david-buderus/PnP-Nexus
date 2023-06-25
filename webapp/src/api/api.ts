/* tslint:disable */
/* eslint-disable */
/**
 * OpenAPI definition
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: v0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


import type { Configuration } from './configuration';
import type { AxiosPromise, AxiosInstance, AxiosRequestConfig } from 'axios';
import globalAxios from 'axios';
// Some imports not used depending on template conditions
// @ts-ignore
import { DUMMY_BASE_URL, assertParamExists, setApiKeyToObject, setBasicAuthToObject, setBearerAuthToObject, setOAuthToObject, setSearchParams, serializeDataIfNeeded, toPathString, createRequestFunction } from './common';
import type { RequestArgs } from './base';
// @ts-ignore
import { BASE_PATH, COLLECTION_FORMATS, BaseAPI, RequiredError } from './base';

/**
 * 
 * @export
 * @interface Armor
 */
export interface Armor {
    /**
     * 
     * @type {ObjectId}
     * @memberof Armor
     */
    'id'?: ObjectId;
    /**
     * 
     * @type {string}
     * @memberof Armor
     */
    'name'?: string;
    /**
     * 
     * @type {ItemType}
     * @memberof Armor
     */
    'type'?: ItemType;
    /**
     * 
     * @type {ItemType}
     * @memberof Armor
     */
    'subtype'?: ItemType;
    /**
     * 
     * @type {string}
     * @memberof Armor
     */
    'requirement'?: string;
    /**
     * 
     * @type {string}
     * @memberof Armor
     */
    'effect'?: string;
    /**
     * 
     * @type {string}
     * @memberof Armor
     */
    'rarity'?: ArmorRarityEnum;
    /**
     * 
     * @type {number}
     * @memberof Armor
     */
    'vendorPrice'?: number;
    /**
     * 
     * @type {number}
     * @memberof Armor
     */
    'tier'?: number;
    /**
     * 
     * @type {string}
     * @memberof Armor
     */
    'description'?: string;
    /**
     * 
     * @type {string}
     * @memberof Armor
     */
    'note'?: string;
    /**
     * 
     * @type {Material}
     * @memberof Armor
     */
    'material'?: Material;
    /**
     * 
     * @type {number}
     * @memberof Armor
     */
    'upgradeSlots'?: number;
    /**
     * 
     * @type {number}
     * @memberof Armor
     */
    'armor'?: number;
    /**
     * 
     * @type {number}
     * @memberof Armor
     */
    'weight'?: number;
    /**
     * 
     * @type {number}
     * @memberof Armor
     */
    'maxDurability'?: number;
}

export const ArmorRarityEnum = {
    Unknown: 'UNKNOWN',
    Common: 'COMMON',
    Rare: 'RARE',
    Epic: 'EPIC',
    Legendary: 'LEGENDARY',
    Godlike: 'GODLIKE'
} as const;

export type ArmorRarityEnum = typeof ArmorRarityEnum[keyof typeof ArmorRarityEnum];

/**
 * 
 * @export
 * @interface ArmorAllOf
 */
export interface ArmorAllOf {
    /**
     * 
     * @type {Material}
     * @memberof ArmorAllOf
     */
    'material'?: Material;
    /**
     * 
     * @type {number}
     * @memberof ArmorAllOf
     */
    'upgradeSlots'?: number;
    /**
     * 
     * @type {number}
     * @memberof ArmorAllOf
     */
    'armor'?: number;
    /**
     * 
     * @type {number}
     * @memberof ArmorAllOf
     */
    'weight'?: number;
    /**
     * 
     * @type {number}
     * @memberof ArmorAllOf
     */
    'maxDurability'?: number;
}
/**
 * @type GetItem200Response
 * @export
 */
export type GetItem200Response = Armor | Item | Jewellery | Shield | Weapon;

/**
 * 
 * @export
 * @interface Item
 */
export interface Item {
    /**
     * 
     * @type {ObjectId}
     * @memberof Item
     */
    'id'?: ObjectId;
    /**
     * 
     * @type {string}
     * @memberof Item
     */
    'name'?: string;
    /**
     * 
     * @type {ItemType}
     * @memberof Item
     */
    'type'?: ItemType;
    /**
     * 
     * @type {ItemType}
     * @memberof Item
     */
    'subtype'?: ItemType;
    /**
     * 
     * @type {string}
     * @memberof Item
     */
    'requirement'?: string;
    /**
     * 
     * @type {string}
     * @memberof Item
     */
    'effect'?: string;
    /**
     * 
     * @type {string}
     * @memberof Item
     */
    'rarity'?: ItemRarityEnum;
    /**
     * 
     * @type {number}
     * @memberof Item
     */
    'vendorPrice'?: number;
    /**
     * 
     * @type {number}
     * @memberof Item
     */
    'tier'?: number;
    /**
     * 
     * @type {string}
     * @memberof Item
     */
    'description'?: string;
    /**
     * 
     * @type {string}
     * @memberof Item
     */
    'note'?: string;
}

export const ItemRarityEnum = {
    Unknown: 'UNKNOWN',
    Common: 'COMMON',
    Rare: 'RARE',
    Epic: 'EPIC',
    Legendary: 'LEGENDARY',
    Godlike: 'GODLIKE'
} as const;

export type ItemRarityEnum = typeof ItemRarityEnum[keyof typeof ItemRarityEnum];

/**
 * 
 * @export
 * @interface ItemStackItem
 */
export interface ItemStackItem {
    /**
     * 
     * @type {number}
     * @memberof ItemStackItem
     */
    'amount'?: number;
    /**
     * 
     * @type {GetItem200Response}
     * @memberof ItemStackItem
     */
    'item'?: GetItem200Response;
}
/**
 * 
 * @export
 * @interface ItemType
 */
export interface ItemType {
    /**
     * 
     * @type {ObjectId}
     * @memberof ItemType
     */
    'id'?: ObjectId;
    /**
     * 
     * @type {string}
     * @memberof ItemType
     */
    'name'?: string;
    /**
     * 
     * @type {string}
     * @memberof ItemType
     */
    'typeRestriction'?: ItemTypeTypeRestrictionEnum;
}

export const ItemTypeTypeRestrictionEnum = {
    Item: 'ITEM',
    Equipment: 'EQUIPMENT',
    Jewellery: 'JEWELLERY',
    Weapon: 'WEAPON',
    Armor: 'ARMOR',
    Shield: 'SHIELD'
} as const;

export type ItemTypeTypeRestrictionEnum = typeof ItemTypeTypeRestrictionEnum[keyof typeof ItemTypeTypeRestrictionEnum];

/**
 * 
 * @export
 * @interface Jewellery
 */
export interface Jewellery {
    /**
     * 
     * @type {ObjectId}
     * @memberof Jewellery
     */
    'id'?: ObjectId;
    /**
     * 
     * @type {string}
     * @memberof Jewellery
     */
    'name'?: string;
    /**
     * 
     * @type {ItemType}
     * @memberof Jewellery
     */
    'type'?: ItemType;
    /**
     * 
     * @type {ItemType}
     * @memberof Jewellery
     */
    'subtype'?: ItemType;
    /**
     * 
     * @type {string}
     * @memberof Jewellery
     */
    'requirement'?: string;
    /**
     * 
     * @type {string}
     * @memberof Jewellery
     */
    'effect'?: string;
    /**
     * 
     * @type {string}
     * @memberof Jewellery
     */
    'rarity'?: JewelleryRarityEnum;
    /**
     * 
     * @type {number}
     * @memberof Jewellery
     */
    'vendorPrice'?: number;
    /**
     * 
     * @type {number}
     * @memberof Jewellery
     */
    'tier'?: number;
    /**
     * 
     * @type {string}
     * @memberof Jewellery
     */
    'description'?: string;
    /**
     * 
     * @type {string}
     * @memberof Jewellery
     */
    'note'?: string;
    /**
     * 
     * @type {Material}
     * @memberof Jewellery
     */
    'material'?: Material;
    /**
     * 
     * @type {number}
     * @memberof Jewellery
     */
    'upgradeSlots'?: number;
    /**
     * 
     * @type {string}
     * @memberof Jewellery
     */
    'gem'?: string;
}

export const JewelleryRarityEnum = {
    Unknown: 'UNKNOWN',
    Common: 'COMMON',
    Rare: 'RARE',
    Epic: 'EPIC',
    Legendary: 'LEGENDARY',
    Godlike: 'GODLIKE'
} as const;

export type JewelleryRarityEnum = typeof JewelleryRarityEnum[keyof typeof JewelleryRarityEnum];

/**
 * 
 * @export
 * @interface JewelleryAllOf
 */
export interface JewelleryAllOf {
    /**
     * 
     * @type {Material}
     * @memberof JewelleryAllOf
     */
    'material'?: Material;
    /**
     * 
     * @type {number}
     * @memberof JewelleryAllOf
     */
    'upgradeSlots'?: number;
    /**
     * 
     * @type {string}
     * @memberof JewelleryAllOf
     */
    'gem'?: string;
}
/**
 * 
 * @export
 * @interface Material
 */
export interface Material {
    /**
     * 
     * @type {ObjectId}
     * @memberof Material
     */
    'id'?: ObjectId;
    /**
     * 
     * @type {string}
     * @memberof Material
     */
    'name'?: string;
    /**
     * 
     * @type {Array<Item>}
     * @memberof Material
     */
    'items'?: Array<Item>;
}
/**
 * 
 * @export
 * @interface ObjectId
 */
export interface ObjectId {
    /**
     * 
     * @type {number}
     * @memberof ObjectId
     */
    'timestamp'?: number;
    /**
     * 
     * @type {string}
     * @memberof ObjectId
     */
    'date'?: string;
}
/**
 * 
 * @export
 * @interface Shield
 */
export interface Shield {
    /**
     * 
     * @type {ObjectId}
     * @memberof Shield
     */
    'id'?: ObjectId;
    /**
     * 
     * @type {string}
     * @memberof Shield
     */
    'name'?: string;
    /**
     * 
     * @type {ItemType}
     * @memberof Shield
     */
    'type'?: ItemType;
    /**
     * 
     * @type {ItemType}
     * @memberof Shield
     */
    'subtype'?: ItemType;
    /**
     * 
     * @type {string}
     * @memberof Shield
     */
    'requirement'?: string;
    /**
     * 
     * @type {string}
     * @memberof Shield
     */
    'effect'?: string;
    /**
     * 
     * @type {string}
     * @memberof Shield
     */
    'rarity'?: ShieldRarityEnum;
    /**
     * 
     * @type {number}
     * @memberof Shield
     */
    'vendorPrice'?: number;
    /**
     * 
     * @type {number}
     * @memberof Shield
     */
    'tier'?: number;
    /**
     * 
     * @type {string}
     * @memberof Shield
     */
    'description'?: string;
    /**
     * 
     * @type {string}
     * @memberof Shield
     */
    'note'?: string;
    /**
     * 
     * @type {Material}
     * @memberof Shield
     */
    'material'?: Material;
    /**
     * 
     * @type {number}
     * @memberof Shield
     */
    'upgradeSlots'?: number;
    /**
     * 
     * @type {number}
     * @memberof Shield
     */
    'initiative'?: number;
    /**
     * 
     * @type {number}
     * @memberof Shield
     */
    'hit'?: number;
    /**
     * 
     * @type {number}
     * @memberof Shield
     */
    'armor'?: number;
    /**
     * 
     * @type {number}
     * @memberof Shield
     */
    'weight'?: number;
    /**
     * 
     * @type {number}
     * @memberof Shield
     */
    'maxDurability'?: number;
}

export const ShieldRarityEnum = {
    Unknown: 'UNKNOWN',
    Common: 'COMMON',
    Rare: 'RARE',
    Epic: 'EPIC',
    Legendary: 'LEGENDARY',
    Godlike: 'GODLIKE'
} as const;

export type ShieldRarityEnum = typeof ShieldRarityEnum[keyof typeof ShieldRarityEnum];

/**
 * 
 * @export
 * @interface ShieldAllOf
 */
export interface ShieldAllOf {
    /**
     * 
     * @type {Material}
     * @memberof ShieldAllOf
     */
    'material'?: Material;
    /**
     * 
     * @type {number}
     * @memberof ShieldAllOf
     */
    'upgradeSlots'?: number;
    /**
     * 
     * @type {number}
     * @memberof ShieldAllOf
     */
    'initiative'?: number;
    /**
     * 
     * @type {number}
     * @memberof ShieldAllOf
     */
    'hit'?: number;
    /**
     * 
     * @type {number}
     * @memberof ShieldAllOf
     */
    'armor'?: number;
    /**
     * 
     * @type {number}
     * @memberof ShieldAllOf
     */
    'weight'?: number;
    /**
     * 
     * @type {number}
     * @memberof ShieldAllOf
     */
    'maxDurability'?: number;
}
/**
 * 
 * @export
 * @interface Weapon
 */
export interface Weapon {
    /**
     * 
     * @type {ObjectId}
     * @memberof Weapon
     */
    'id'?: ObjectId;
    /**
     * 
     * @type {string}
     * @memberof Weapon
     */
    'name'?: string;
    /**
     * 
     * @type {ItemType}
     * @memberof Weapon
     */
    'type'?: ItemType;
    /**
     * 
     * @type {ItemType}
     * @memberof Weapon
     */
    'subtype'?: ItemType;
    /**
     * 
     * @type {string}
     * @memberof Weapon
     */
    'requirement'?: string;
    /**
     * 
     * @type {string}
     * @memberof Weapon
     */
    'effect'?: string;
    /**
     * 
     * @type {string}
     * @memberof Weapon
     */
    'rarity'?: WeaponRarityEnum;
    /**
     * 
     * @type {number}
     * @memberof Weapon
     */
    'vendorPrice'?: number;
    /**
     * 
     * @type {number}
     * @memberof Weapon
     */
    'tier'?: number;
    /**
     * 
     * @type {string}
     * @memberof Weapon
     */
    'description'?: string;
    /**
     * 
     * @type {string}
     * @memberof Weapon
     */
    'note'?: string;
    /**
     * 
     * @type {Material}
     * @memberof Weapon
     */
    'material'?: Material;
    /**
     * 
     * @type {number}
     * @memberof Weapon
     */
    'upgradeSlots'?: number;
    /**
     * 
     * @type {number}
     * @memberof Weapon
     */
    'initiative'?: number;
    /**
     * 
     * @type {number}
     * @memberof Weapon
     */
    'hit'?: number;
    /**
     * 
     * @type {number}
     * @memberof Weapon
     */
    'damage'?: number;
    /**
     * 
     * @type {string}
     * @memberof Weapon
     */
    'dice'?: string;
    /**
     * 
     * @type {number}
     * @memberof Weapon
     */
    'maxDurability'?: number;
}

export const WeaponRarityEnum = {
    Unknown: 'UNKNOWN',
    Common: 'COMMON',
    Rare: 'RARE',
    Epic: 'EPIC',
    Legendary: 'LEGENDARY',
    Godlike: 'GODLIKE'
} as const;

export type WeaponRarityEnum = typeof WeaponRarityEnum[keyof typeof WeaponRarityEnum];

/**
 * 
 * @export
 * @interface WeaponAllOf
 */
export interface WeaponAllOf {
    /**
     * 
     * @type {Material}
     * @memberof WeaponAllOf
     */
    'material'?: Material;
    /**
     * 
     * @type {number}
     * @memberof WeaponAllOf
     */
    'upgradeSlots'?: number;
    /**
     * 
     * @type {number}
     * @memberof WeaponAllOf
     */
    'initiative'?: number;
    /**
     * 
     * @type {number}
     * @memberof WeaponAllOf
     */
    'hit'?: number;
    /**
     * 
     * @type {number}
     * @memberof WeaponAllOf
     */
    'damage'?: number;
    /**
     * 
     * @type {string}
     * @memberof WeaponAllOf
     */
    'dice'?: string;
    /**
     * 
     * @type {number}
     * @memberof WeaponAllOf
     */
    'maxDurability'?: number;
}

/**
 * ItemServiceApi - axios parameter creator
 * @export
 */
export const ItemServiceApiAxiosParamCreator = function (configuration?: Configuration) {
    return {
        /**
         * 
         * @summary Get an item
         * @param {string} universe 
         * @param {string} name 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        getItem: async (universe: string, name: string, options: AxiosRequestConfig = {}): Promise<RequestArgs> => {
            // verify required parameter 'universe' is not null or undefined
            assertParamExists('getItem', 'universe', universe)
            // verify required parameter 'name' is not null or undefined
            assertParamExists('getItem', 'name', name)
            const localVarPath = `/{universe}/item/{name}`
                .replace(`{${"universe"}}`, encodeURIComponent(String(universe)))
                .replace(`{${"name"}}`, encodeURIComponent(String(name)));
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'GET', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;


    
            setSearchParams(localVarUrlObj, localVarQueryParameter);
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};

            return {
                url: toPathString(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
        /**
         * 
         * @summary Get an item
         * @param {string} universe 
         * @param {string} name 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        getItem1: async (universe: string, name: string, options: AxiosRequestConfig = {}): Promise<RequestArgs> => {
            // verify required parameter 'universe' is not null or undefined
            assertParamExists('getItem1', 'universe', universe)
            // verify required parameter 'name' is not null or undefined
            assertParamExists('getItem1', 'name', name)
            const localVarPath = `/{universe}/a/{name}`
                .replace(`{${"universe"}}`, encodeURIComponent(String(universe)))
                .replace(`{${"name"}}`, encodeURIComponent(String(name)));
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'GET', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;


    
            setSearchParams(localVarUrlObj, localVarQueryParameter);
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};

            return {
                url: toPathString(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
        /**
         * 
         * @param {string} universe 
         * @param {string} name 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        itemStack: async (universe: string, name: string, options: AxiosRequestConfig = {}): Promise<RequestArgs> => {
            // verify required parameter 'universe' is not null or undefined
            assertParamExists('itemStack', 'universe', universe)
            // verify required parameter 'name' is not null or undefined
            assertParamExists('itemStack', 'name', name)
            const localVarPath = `/{universe}/itemstack/{name}`
                .replace(`{${"universe"}}`, encodeURIComponent(String(universe)))
                .replace(`{${"name"}}`, encodeURIComponent(String(name)));
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'GET', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;


    
            setSearchParams(localVarUrlObj, localVarQueryParameter);
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};

            return {
                url: toPathString(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
    }
};

/**
 * ItemServiceApi - functional programming interface
 * @export
 */
export const ItemServiceApiFp = function(configuration?: Configuration) {
    const localVarAxiosParamCreator = ItemServiceApiAxiosParamCreator(configuration)
    return {
        /**
         * 
         * @summary Get an item
         * @param {string} universe 
         * @param {string} name 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async getItem(universe: string, name: string, options?: AxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<GetItem200Response>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.getItem(universe, name, options);
            return createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration);
        },
        /**
         * 
         * @summary Get an item
         * @param {string} universe 
         * @param {string} name 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async getItem1(universe: string, name: string, options?: AxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<GetItem200Response>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.getItem1(universe, name, options);
            return createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration);
        },
        /**
         * 
         * @param {string} universe 
         * @param {string} name 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async itemStack(universe: string, name: string, options?: AxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<ItemStackItem>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.itemStack(universe, name, options);
            return createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration);
        },
    }
};

/**
 * ItemServiceApi - factory interface
 * @export
 */
export const ItemServiceApiFactory = function (configuration?: Configuration, basePath?: string, axios?: AxiosInstance) {
    const localVarFp = ItemServiceApiFp(configuration)
    return {
        /**
         * 
         * @summary Get an item
         * @param {string} universe 
         * @param {string} name 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        getItem(universe: string, name: string, options?: any): AxiosPromise<GetItem200Response> {
            return localVarFp.getItem(universe, name, options).then((request) => request(axios, basePath));
        },
        /**
         * 
         * @summary Get an item
         * @param {string} universe 
         * @param {string} name 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        getItem1(universe: string, name: string, options?: any): AxiosPromise<GetItem200Response> {
            return localVarFp.getItem1(universe, name, options).then((request) => request(axios, basePath));
        },
        /**
         * 
         * @param {string} universe 
         * @param {string} name 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        itemStack(universe: string, name: string, options?: any): AxiosPromise<ItemStackItem> {
            return localVarFp.itemStack(universe, name, options).then((request) => request(axios, basePath));
        },
    };
};

/**
 * ItemServiceApi - object-oriented interface
 * @export
 * @class ItemServiceApi
 * @extends {BaseAPI}
 */
export class ItemServiceApi extends BaseAPI {
    /**
     * 
     * @summary Get an item
     * @param {string} universe 
     * @param {string} name 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof ItemServiceApi
     */
    public getItem(universe: string, name: string, options?: AxiosRequestConfig) {
        return ItemServiceApiFp(this.configuration).getItem(universe, name, options).then((request) => request(this.axios, this.basePath));
    }

    /**
     * 
     * @summary Get an item
     * @param {string} universe 
     * @param {string} name 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof ItemServiceApi
     */
    public getItem1(universe: string, name: string, options?: AxiosRequestConfig) {
        return ItemServiceApiFp(this.configuration).getItem1(universe, name, options).then((request) => request(this.axios, this.basePath));
    }

    /**
     * 
     * @param {string} universe 
     * @param {string} name 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof ItemServiceApi
     */
    public itemStack(universe: string, name: string, options?: AxiosRequestConfig) {
        return ItemServiceApiFp(this.configuration).itemStack(universe, name, options).then((request) => request(this.axios, this.basePath));
    }
}


