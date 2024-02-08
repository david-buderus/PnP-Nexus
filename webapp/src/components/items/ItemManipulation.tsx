import { Autocomplete, Stack, TextField } from "@mui/material";
import { ERarity, ItemType, Material, Weapon } from "../../api";
import { useEffect, useState } from "react";
import { RaritySelect } from "../inputs/RaritySelect";
import { useTranslation } from "react-i18next";
import { getUniverseContext } from "../PageBase";
import { currencyToHumanReadable } from "../Utils";
import { TFunction } from "i18next";
import { ItemClass, SomeItem } from "../Constants";
import { TextFieldWithError, TextFieldWithErrorForAutoComplete } from "../inputs/TestFieldWithError";

function createItem(itemClass: string, id: string, name: string, type: ItemType, subtype: ItemType, rarity: ERarity, tier: string, effect: string, description: string, requirement: string, price: string, note: string, material: Material, upgradeSlots: string, hit: string, initiative: string, damage: string, dice: string, weight: string, armor: string, minStackSize: string, maxStackSize: string) {
    switch (itemClass) {
        case "Weapon":
            return {
                id: id,
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
        case "Shield":
            return {
                id: id,
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
        case "Armor":
            return {
                id: id,
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
        case "Jewellery":
            return {
                id: id,
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
        default:
            return {
                id: id,
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
    }
}

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
    const [tier, setTier] = useState<string>(item?.tier?.toString() ?? "1");
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
        let newItem: SomeItem = createItem(itemClass, item?.id, name, type, subtype, rarity, tier, effect, description, requirement, price, note, material, upgradeSlots, hit, initiative, damage, dice, weight, armor, minStackSize, maxStackSize);
        newItem["@type"] = itemClass;
        setItem(newItem);
    }, [itemClass, name, type, subtype, rarity, tier, effect, description, requirement, price, minStackSize, maxStackSize, note, material, upgradeSlots, hit, initiative, weight, armor, damage, dice]);

    return <Stack spacing={2}>
        <TextFieldWithError fieldId="name" errorMap={errors} value={name} onChange={setName} label={t("name")} fullWidth />
        <Stack direction="row" spacing={2}>
            <Autocomplete
                fullWidth
                disablePortal
                options={itemTypes}
                getOptionLabel={(option: ItemType) => {
                    return option?.name;
                }}
                isOptionEqualToValue={(option: ItemType, value: ItemType) => option.id === value.id}
                renderInput={(params) => <TextFieldWithErrorForAutoComplete {...params} fieldId="type" errorMap={errors} label={t("item:type")} />}
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
                renderInput={(params) => <TextFieldWithErrorForAutoComplete {...params} fieldId="subtype" errorMap={errors} label={t("item:subtype")} />}
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
                renderInput={(params) => <TextFieldWithErrorForAutoComplete {...params} fieldId="material" errorMap={errors} label={t("material")} />}
                value={material ?? null}
                onChange={(_, value) => { setMaterial(value); }}
                data-testid="material"
            />
        }
        {(itemClass === "Armor" || itemClass === "Shield") &&
            <Stack direction="row" spacing={2}>
                <TextFieldWithError fieldId="weight" errorMap={errors} numberField label={t("weight")} value={weight} onChange={setWeight} fullWidth />
                <TextFieldWithError fieldId="armor" errorMap={errors} integerField label={t("armor")} value={armor} onChange={setArmor} fullWidth />
            </Stack>
        }
        {itemClass === "Weapon" &&
            <Stack direction="row" spacing={2}>
                <TextFieldWithError fieldId="damage" errorMap={errors} integerField label={t("damage")} value={damage} onChange={setDamage} fullWidth />
                <TextFieldWithError fieldId="dice" errorMap={errors} label={t("dice")} value={dice} onChange={setDice} fullWidth />
            </Stack>
        }
        {(itemClass === "Weapon" || itemClass === "Shield") &&
            <Stack direction="row" spacing={2}>
                <TextFieldWithError fieldId="hit" errorMap={errors} integerField label={t("hit")} value={hit} onChange={setHit} fullWidth />
                <TextFieldWithError fieldId="initiative" errorMap={errors} numberField label={t("initiative")} value={initiative} onChange={setInitiative} fullWidth />
            </Stack>
        }
        <TextFieldWithError fieldId="effect" errorMap={errors} label={t("effect")} multiline rows={2} value={effect} onChange={setEffect} />
        <TextFieldWithError fieldId="description" errorMap={errors} label={t("description")} multiline rows={2} value={description} onChange={setDescription} />
        {itemClass !== "Item" &&
            <TextFieldWithError fieldId="fieldId" errorMap={errors} integerField label={t("upgradeSlots")} value={upgradeSlots} onChange={setUpgradeSlots} />
        }
        <Stack direction="row" spacing={2}>
            <RaritySelect
                {...(errors["rarity"] !== undefined ? {
                    error: true,
                    helperText: errors["rarity"]
                } : {})}
                data-testid="rarity"
                value={rarity}
                onChange={setRarity}
                fullWidth
            />
            <TextFieldWithError fieldId="tier" errorMap={errors} integerField label={t("tier")} value={tier} onChange={setTier} fullWidth />
        </Stack>
        <TextFieldWithError fieldId="requirement" errorMap={errors} label={t("requirement")} multiline rows={2} value={requirement} onChange={setRequirement} />
        <Stack direction="row" spacing={2}>
            <TextFieldWithError fieldId="vendorPrice" errorMap={errors} integerField label={t("price")} value={price} onChange={setPrice} fullWidth />
            <TextField label={t("item:resultingPrice")} data-testid="resultingPrice" variant="outlined" value={currencyToHumanReadable(activeUniverse, Number(price))} InputProps={{ readOnly: true }} fullWidth />
        </Stack>
        {itemClass === "Item" && <Stack direction="row" spacing={2}>
            <TextFieldWithError fieldId="minimumStackSize" errorMap={errors} integerField label={t("item:minStackSize")} value={minStackSize} onChange={setMinStackSize} fullWidth />
            <TextFieldWithError fieldId="maximumStackSize" errorMap={errors} integerField label={t("item:maxStackSize")} value={maxStackSize} onChange={setMaxStackSize} fullWidth />
        </Stack>}
        <TextFieldWithError fieldId="note" errorMap={errors} label={t("note")} multiline rows={2} value={note} onChange={setNote} />
    </Stack>;
}
