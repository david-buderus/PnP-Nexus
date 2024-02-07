import { Autocomplete, Stack, TextField } from "@mui/material";
import { ERarity, ItemType, Material } from "../../api";
import { useEffect, useState } from "react";
import { RaritySelect } from "../inputs/RaritySelect";
import { useTranslation } from "react-i18next";
import { getUniverseContext } from "../PageBase";
import { currencyToHumanReadable } from "../Utils";
import { TFunction } from "i18next";
import { ItemClass, SomeItem } from "../Constants";

/** Props needed for the item manipulation */
interface ItemManipulationProps {
    /** The known item types */
    itemTypes: ItemType[];
    /** The known materials */
    materials: Material[];
    /** The item class which gets manipulated */
    itemClass: ItemClass;
    /** The current errors */
    errors: Map<string, string>;
    /** The optional item which can get manipulated */
    item?: SomeItem;
    /** Hook to the current item */
    setItem: (item: SomeItem) => void;
}

function errorMessage(id: string, errorMap: Map<string, string>, int?: boolean, content?: string, t?: TFunction<"translation", undefined>) {
    if (content !== undefined) {
        const n = Number(content);
        if (Number.isNaN(n)) {
            return {
                id: id,
                error: true,
                helperText: t('error:notNumber')
            };
        }
        if (int && !Number.isInteger(n)) {
            return {
                id: id,
                error: true,
                helperText: t('error:notInteger')
            };
        }
    }
    if (errorMap[id]) {
        return {
            id: id,
            error: true,
            helperText: errorMap[id]
        };
    }
    return {
        id: id
    };
}

/**
 * All text fields needed to manipulate/create items.
 */
export function ItemManipulation(props: ItemManipulationProps) {
    const { itemTypes, materials, itemClass, errors, item, setItem } = props;
    const { t } = useTranslation();
    const { activeUniverse } = getUniverseContext();

    const [name, setName] = useState<string>(item?.name ?? "");
    const [type, setType] = useState<ItemType>(item?.type ?? null);
    const [subtype, setSubtype] = useState<ItemType>(item?.subtype ?? null);
    const [rarity, setRarity] = useState<ERarity>(item?.rarity ?? ERarity.Common);
    const [tier, setTier] = useState<string>(item?.tier?.toString() ?? "");
    const [effect, setEffect] = useState<string>(item?.effect ?? "");
    const [description, setDescription] = useState<string>(item?.description ?? "");
    const [requirement, setRequirement] = useState<string>(item?.requirement ?? "");
    const [price, setPrice] = useState<string>(item?.vendorPrice?.toString() ?? "");
    const [minStackSize, setMinStackSize] = useState<string>(item?.minimumStackSize?.toString() ?? "0");
    const [maxStackSize, setMaxStackSize] = useState<string>(item?.maximumStackSize?.toString() ?? "100");
    const [note, setNote] = useState<string>(item?.note ?? "");

    const [material, setMaterial] = useState<Material>(item ? item["material"] ?? null : null);
    const [upgradeSlots, setUpgradeSlots] = useState<string>(item ? item["upgradeSlots"] ?? "0" : "0");

    const [hit, setHit] = useState<string>(item ? item["hit"] ?? "0" : "0");
    const [initiative, setInitiative] = useState<string>(item ? item["initiative"] ?? "0" : "0");

    const [weight, setWeight] = useState<string>(item ? item["weight"] ?? "0" : "0");
    const [armor, setArmor] = useState<string>(item ? item["armor"] ?? "0" : "0");

    const [damage, setDamage] = useState<string>(item ? item["damage"] ?? "0" : "0");
    const [dice, setDice] = useState<string>(item ? item["dice"] ?? "D6" : "D6");

    useEffect(() => {
        let newItem: SomeItem;
        switch (itemClass) {
            case "Weapon":
                newItem = {
                    id: item?.id,
                    name: name,
                    type: type,
                    subtype: subtype,
                    rarity: rarity,
                    tier: Number(tier),
                    effect: effect,
                    description: description,
                    requirement: requirement,
                    vendorPrice: Number(price),
                    minimumStackSize: 1,
                    maximumStackSize: 1,
                    note: note,
                    material: material,
                    upgradeSlots: Number(upgradeSlots),
                    hit: Number(hit),
                    initiative: Number(initiative),
                    damage: Number(damage),
                    dice: dice
                };
                break;
            case "Shield":
                newItem = {
                    id: item?.id,
                    name: name,
                    type: type,
                    subtype: subtype,
                    rarity: rarity,
                    tier: Number(tier),
                    effect: effect,
                    description: description,
                    requirement: requirement,
                    vendorPrice: Number(price),
                    minimumStackSize: 1,
                    maximumStackSize: 1,
                    note: note,
                    material: material,
                    upgradeSlots: Number(upgradeSlots),
                    hit: Number(hit),
                    initiative: Number(initiative),
                    weight: Number(weight),
                    armor: Number(armor)
                };
                break;
            case "Armor":
                newItem = {
                    id: item?.id,
                    name: name,
                    type: type,
                    subtype: subtype,
                    rarity: rarity,
                    tier: Number(tier),
                    effect: effect,
                    description: description,
                    requirement: requirement,
                    vendorPrice: Number(price),
                    minimumStackSize: 1,
                    maximumStackSize: 1,
                    note: note,
                    material: material,
                    upgradeSlots: Number(upgradeSlots),
                    weight: Number(weight),
                    armor: Number(armor)
                };
                break;
            case "Jewellery":
                newItem = {
                    id: item?.id,
                    name: name,
                    type: type,
                    subtype: subtype,
                    rarity: rarity,
                    tier: Number(tier),
                    effect: effect,
                    description: description,
                    requirement: requirement,
                    vendorPrice: Number(price),
                    minimumStackSize: 1,
                    maximumStackSize: 1,
                    note: note,
                    material: material,
                    upgradeSlots: Number(upgradeSlots)
                };
                break;
            default:
                newItem = {
                    id: item?.id,
                    name: name,
                    type: type,
                    subtype: subtype,
                    rarity: rarity,
                    tier: Number(tier),
                    effect: effect,
                    description: description,
                    requirement: requirement,
                    vendorPrice: Number(price),
                    minimumStackSize: Number(minStackSize),
                    maximumStackSize: Number(maxStackSize),
                    note: note
                };
                break;
        }
        newItem["@type"] = itemClass;
        setItem(newItem);
    }, [itemClass, name, type, subtype, rarity, tier, effect, description, requirement, price, minStackSize, maxStackSize, note, material, upgradeSlots, hit, initiative, weight, armor, damage, dice]);

    return <Stack spacing={2}>
        <TextField {...errorMessage("name", errors)} data-testid="name" label={t("name")} variant="outlined" value={name} onChange={event => setName(event.target.value)} fullWidth />
        <Stack direction="row" spacing={2}>
            <Autocomplete
                fullWidth
                disablePortal
                options={itemTypes}
                getOptionLabel={(option: ItemType) => {
                    return option?.name;
                }}
                isOptionEqualToValue={(option: ItemType, value: ItemType) => option.id === value.id}
                renderInput={(params) => <TextField {...params} {...errorMessage("type", errors)} label={t("item:type")} />}
                value={type ?? null}
                onChange={(_, value) => { setType(value); }}
                data-testid="type"
            />
            <Autocomplete
                fullWidth
                disablePortal
                options={itemTypes}
                getOptionLabel={(option: ItemType) => {
                    return option?.name;
                }}
                isOptionEqualToValue={(option: ItemType, value: ItemType) => option.id === value.id}
                renderInput={(params) => <TextField {...params} {...errorMessage("subtype", errors)} label={t("item:subtype")} />}
                value={subtype ?? null}
                onChange={(_, value) => { setSubtype(value); }}
                data-testid="subtype"
            />
        </Stack>
        {itemClass !== "Item" &&
            <Autocomplete
                disablePortal
                options={materials}
                getOptionLabel={(option: Material) => {
                    return option?.name;
                }}
                isOptionEqualToValue={(option: Material, value: Material) => option.id === value.id}
                renderInput={(params) => <TextField {...params} {...errorMessage("material", errors)} label={t("material")} />}
                value={material ?? null}
                onChange={(_, value) => { setMaterial(value); }}
                data-testid="material"
            />
        }
        {(itemClass === "Armor" || itemClass === "Shield") &&
            <Stack direction="row" spacing={2}>
                <TextField {...errorMessage("weight", errors, false, weight, t)} data-testid="weight" label={t("weight")} variant="outlined" value={weight} onChange={event => setWeight(event.target.value)} fullWidth />
                <TextField {...errorMessage("armor", errors, true, armor, t)} data-testid="armor" label={t("armor")} variant="outlined" value={armor} onChange={event => setArmor(event.target.value)} fullWidth />
            </Stack>
        }
        {itemClass === "Weapon" &&
            <Stack direction="row" spacing={2}>
                <TextField {...errorMessage("damage", errors, true, damage, t)} data-testid="damage" label={t("damage")} variant="outlined" value={damage} onChange={event => setDamage(event.target.value)} fullWidth />
                <TextField {...errorMessage("dice", errors)} label={t("dice")} data-testid="dice" variant="outlined" value={dice} onChange={event => setDice(event.target.value)} fullWidth />
            </Stack>
        }
        {(itemClass === "Weapon" || itemClass === "Shield") &&
            <Stack direction="row" spacing={2}>
                <TextField {...errorMessage("hit", errors, true, hit, t)} data-testid="hit" label={t("hit")} variant="outlined" value={hit} onChange={event => setHit(event.target.value)} fullWidth />
                <TextField {...errorMessage("initiative", errors, false, initiative, t)} data-testid="initiative" label={t("initiative")} variant="outlined" value={initiative} onChange={event => setInitiative(event.target.value)} fullWidth />
            </Stack>
        }
        <TextField {...errorMessage("effect", errors)} label={t("effect")} data-testid="effect" variant="outlined" multiline rows={2} value={effect} onChange={event => setEffect(event.target.value)} />
        <TextField {...errorMessage("description", errors)} label={t("description")} data-testid="description" variant="outlined" multiline rows={2} value={description} onChange={event => setDescription(event.target.value)} />
        {itemClass !== "Item" &&
            <TextField {...errorMessage("upgradeSlots", errors, true, upgradeSlots, t)} data-testid="upgradeSlots" label={t("upgradeSlots")} variant="outlined" value={upgradeSlots} onChange={event => setUpgradeSlots(event.target.value)} />
        }
        <Stack direction="row" spacing={2}>
            <RaritySelect
                {...errorMessage("rarity", errors)}
                data-testid="rarity"
                value={rarity}
                onChange={setRarity}
                fullWidth
            />
            <TextField {...errorMessage("tier", errors, true, tier, t)} data-testid="tier" label={t("tier")} variant="outlined" value={tier} onChange={event => setTier(event.target.value)} fullWidth />
        </Stack>
        <TextField {...errorMessage("requirement", errors)} data-testid="requirement" label={t("requirement")} variant="outlined" multiline rows={2} value={requirement} onChange={event => setRequirement(event.target.value)} />
        <Stack direction="row" spacing={2}>
            <TextField {...errorMessage("vendorPrice", errors, true, price, t)} data-testid="vendorPrice" label={t("price")} variant="outlined" value={price} onChange={event => setPrice(event.target.value)} fullWidth />
            <TextField label={t("item:resultingPrice")} data-testid="resultingPrice" variant="outlined" value={currencyToHumanReadable(activeUniverse, Number(price))} InputProps={{ readOnly: true }} fullWidth />
        </Stack>
        {itemClass === "Item" && <Stack direction="row" spacing={2}>
            <TextField {...errorMessage("minimumStackSize", errors, true, minStackSize, t)} data-testid="minimumStackSize" label={t("item:minStackSize")} variant="outlined" value={minStackSize} onChange={event => setMinStackSize(event.target.value)} fullWidth />
            <TextField {...errorMessage("maximumStackSize", errors, true, maxStackSize, t)} data-testid="maximumStackSize" label={t("item:maxStackSize")} variant="outlined" value={maxStackSize} onChange={event => setMaxStackSize(event.target.value)} fullWidth />
        </Stack>}
        <TextField {...errorMessage("note", errors)} data-testid="note" label={t("note")} variant="outlined" multiline rows={2} value={note} onChange={event => setNote(event.target.value)} />
    </Stack>;
}
