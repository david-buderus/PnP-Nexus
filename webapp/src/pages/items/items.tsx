import { getUniverseContext } from '../../components/PageBase';
import { currencyToHumanReadable } from '../../components/Utils';
import { useTranslation } from 'react-i18next';
import { Armor, EArmorSlot, ERarity, Item, ItemServiceApi, Jewellery, Shield, Weapon } from '../../api';
import { OverviewBasePage } from '../../components/database/OverviewBasePage';
import { API_CONFIGURATION, SomeItem } from '../../components/Constants';
import { fetchAllMaterials } from '../../components/Database';
import { TFunction } from 'i18next';
import { DatabaseObjectDialogField } from '../../components/database/DatabaseObjectDialog';
import { AxiosResponse } from 'axios';

const ITEM_API = new ItemServiceApi(API_CONFIGURATION);
type ItemCombination = Item & Partial<Weapon> & Partial<Shield> & Partial<Armor> & Partial<Jewellery>;

const ItemPage = () => {
    const { t } = useTranslation();
    const { currencySettings } = getUniverseContext();

    return <OverviewBasePage<ItemCombination>
        columns={[
            { label: t("name"), id: "name", getter: item => item.name },
            { label: t("item:tags"), id: "tags", getter: item => Array.from(item.tags).join(", ") },
            { label: t("rarity"), id: "rarity", getter: item => t("enum:" + item.rarity.toLowerCase()) },
            { label: t("tier"), id: "tier", getter: item => item.tier, numeric: true },
            { label: t("effect"), id: "effect", getter: item => item.effect },
            { label: t("description"), id: "description", getter: item => item.description },
            { label: t("requirement"), id: "requirement", getter: item => item.requirement },
            { label: t("price"), id: "vendorPrice", getter: item => currencyToHumanReadable(currencySettings, item.vendorPrice) },
            { label: t("item:minStackSize"), id: "minimumStackSize", getter: item => item.minimumStackSize, numeric: true, defaultVisible: false },
            { label: t("item:maxStackSize"), id: "maximumStackSize", getter: item => item.maximumStackSize, numeric: true, defaultVisible: false },
            { label: t("note"), id: "note", getter: item => item.note, defaultVisible: false }
        ]}
        fields={createItemFields()}
        sortingKey="name"
        creationDialogTitle={t("item:creationTitle")}
        editDialogTitle={t("item:editTitle")}
        deletionDialogTitle={t("upgrade:confirmDeletionTitle")}
        fetchObjects={universe => ITEM_API.getAllItems(universe)}
        removeObjects={(universe, selected) => ITEM_API.deleteAllItems(universe, selected)}
        editObject={(universe, id, itemType) => ITEM_API.updateItem(universe, id, itemType)}
        createObjects={(universe, objs) => {
            console.log(objs);
            return ITEM_API.insertAllItems(universe, objs);
        }}
        emptyObject={{
            "@type": "Item",
            name: "",
            tags: new Set<string>(),
            rarity: ERarity.Common,
            tier: 1,
            effect: "",
            description: "",
            requirement: "",
            vendorPrice: 0,
            minimumStackSize: 0,
            maximumStackSize: 100,
            note: ""
        }}
    />;
};

export function createItemFields(): DatabaseObjectDialogField<ItemCombination, any>[] {
    const { t } = useTranslation();
    const [materials] = fetchAllMaterials();

    return [
        {
            fieldId: "@type", label: "", fieldType: "ENUM", dependency: [
                { key: "Item", content: "Item", label: t("item") },
                { key: "Weapon", content: "Weapon", label: t("weapon") },
                { key: "Shield", content: "Shield", label: t("shield") },
                { key: "Armor", content: "Armor", label: t("armor") },
                { key: "Jewellery", content: "Jewellery", label: t("jewellery") }
            ]
        },
        { fieldId: "name", label: t("name"), fieldType: "STRING" },
        { fieldId: "tags", label: t("item:tags"), fieldType: "STRING_SET" },
        {
            fieldId: "material", label: t("material"), fieldType: "DATABASE", dependency: materials, dependencyLabel: "name",
            visibleForTypes: ["Weapon", "Shield", "Armor", "Jewellery"]
        },
        {
            fieldId: "armorSlot", label: t("slot"), fieldType: "ENUM", dependency: Object.values(EArmorSlot).map(rarity => {
                return { key: rarity, content: rarity, label: t("enum:" + rarity.toLowerCase()) };
            }),
            visibleForTypes: ["Armor"]
        },
        {
            fieldId: "armor-row", label: "", fieldType: "STACK", subFields: [
                { fieldId: "armor", label: t("armor"), fieldType: "NUMBER" },
                { fieldId: "weight", label: t("weight"), fieldType: "NUMBER" },
            ],
            visibleForTypes: ["Armor", "Shield"]
        },
        {
            fieldId: "protection", label: t("protection"), fieldType: "NUMBER", visibleForTypes: ["Armor"]
        },
        {
            fieldId: "weapon-row", label: "", fieldType: "STACK", subFields: [
                { fieldId: "damage", label: t("damage"), fieldType: "NUMBER" },
                { fieldId: "dice", label: t("dice"), fieldType: "DICE" },
            ],
            visibleForTypes: ["Weapon"]
        },
        {
            fieldId: "hand-row", label: "", fieldType: "STACK", subFields: [
                { fieldId: "hit", label: t("hit"), fieldType: "NUMBER" },
                { fieldId: "initiative", label: t("initiative"), fieldType: "NUMBER" },
            ],
            visibleForTypes: ["Weapon", "Shield"]
        },
        { fieldId: "effect", label: t("effect"), fieldType: "STRING", multiline: true },
        { fieldId: "description", label: t("description"), fieldType: "STRING", multiline: true },
        { fieldId: "upgradeSlots", label: t("upgradeSlots"), fieldType: "NUMBER", visibleForTypes: ["Weapon", "Shield", "Armor", "Jewellery"] },
        {
            fieldId: "rarity-row", label: "", fieldType: "STACK", subFields: [
                {
                    fieldId: "rarity", label: t("rarity"), fieldType: "ENUM", dependency: Object.values(ERarity).map(rarity => {
                        return { key: rarity, content: rarity, label: t("enum:" + rarity.toLowerCase()) };
                    })
                },
                { fieldId: "tier", label: t("tier"), fieldType: "NUMBER" },
            ]
        },
        { fieldId: "requirement", label: t("requirement"), fieldType: "STRING" },
        { fieldId: "vendorPrice", label: t("price"), fieldType: "PRICE" },
        {
            fieldId: "stack-row", label: "", fieldType: "STACK", subFields: [
                { fieldId: "minimumStackSize", label: t("item:minStackSize"), fieldType: "NUMBER" },
                { fieldId: "maximumStackSize", label: t("item:maxStackSize"), fieldType: "NUMBER" },
            ],
            visibleForTypes: ["Weapon", "Shield"]
        },
        { fieldId: "note", label: t("note"), fieldType: "STRING" },
    ];
}

/** Maps an item response */
export function mapResponse<I>(response: AxiosResponse<SomeItem, any>): AxiosResponse<I, any> {
    return {
        ...response,
        data: response.data as I
    };
}

/** Maps an item response */
export function mapArrayResponse<I>(response: AxiosResponse<SomeItem[], any>): AxiosResponse<I[], any> {
    return {
        ...response,
        data: response.data as I[]
    };
}

export default ItemPage;
