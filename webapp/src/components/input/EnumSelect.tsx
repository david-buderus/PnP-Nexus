import { MultiSelect, MultiSelectProps, Select, SelectProps } from "@mantine/core";
import { useTranslation } from "react-i18next";
import { EAction, EArmorSlot, ECalculation, ECastingType, ERarity, EUpgradeEquipmentManipulator, EUpgradeRestriction } from "../../api";

/** Props needed for the select */
interface EnumSelectProps<E> extends Omit<SelectProps, "value" | "onChange" | "data"> {
    /** The value */
    value?: E;
    /** On change handler */
    onChange?: (v: E) => void;
}

/** Props needed for the multiselect */
interface EnumMultiSelectProps<E> extends Omit<MultiSelectProps, "value" | "onChange" | "data"> {
    /** The value */
    value?: E[];
    /** On change handler */
    onChange?: (v: E[]) => void;
}

export function RaritySelect({ value, onChange, ...rest }: EnumSelectProps<ERarity>) {
    const { t } = useTranslation();

    return <Select
        label={t("rarity")}
        value={value}
        onChange={onChange}
        data={Object.values(ERarity).map(rarity => { return { value: rarity, label: t("enum:" + rarity.toLowerCase()) }; })}
        {...rest}
    />;
}

export function ArmorSlotSelect({ value, onChange, ...rest }: EnumSelectProps<EArmorSlot>) {
    const { t } = useTranslation();

    return <Select
        label={t("item:armorSlot")}
        value={value}
        onChange={onChange}
        data={Object.values(EArmorSlot).map(slot => { return { value: slot, label: t("enum:" + slot.toLowerCase()) }; })}
        {...rest}
    />;
}

export function UpgradeRestrictionSelect({ value, onChange, ...rest }: EnumSelectProps<EUpgradeRestriction>) {
    const { t } = useTranslation();

    return <Select
        label={t("upgrade:restriction")}
        value={value}
        onChange={onChange}
        data={Object.values(EUpgradeRestriction).map(slot => { return { value: slot, label: t("enum:" + slot.toLowerCase()) }; })}
        {...rest}
    />;
}

export function UpgradeEquipmentManipulatorSelect({ value, onChange, ...rest }: EnumSelectProps<EUpgradeEquipmentManipulator>) {
    const { t } = useTranslation();

    return <Select
        label={t("upgrade:upgradeManipulator")}
        value={value}
        onChange={onChange}
        data={Object.values(EUpgradeEquipmentManipulator).map(slot => { return { value: slot, label: t("enum:" + slot.toLowerCase()) }; })}
        {...rest}
    />;
}

export function CalculationSelect({ value, onChange, ...rest }: EnumSelectProps<ECalculation>) {
    const { t } = useTranslation();

    return <Select
        label={t("upgrade:calculation")}
        value={value}
        onChange={onChange}
        data={Object.values(ECalculation).map(slot => { return { value: slot, label: t("enum:" + slot.toLowerCase()) }; })}
        {...rest}
    />;
}

export function ActionSelect({ value, onChange, ...rest }: EnumSelectProps<EAction>) {
    const { t } = useTranslation();

    return <Select
        label={t("enum:action")}
        value={value}
        onChange={onChange}
        data={Object.values(EAction).map(action => { return { value: action, label: t("enum:" + action.toLowerCase()) }; })}
        {...rest}
    />;
}

export function CastingTypeMultiSelect({ value, onChange, ...rest }: EnumMultiSelectProps<ECastingType>) {
    const { t } = useTranslation();

    return <MultiSelect
        label={t("spell:castingtype")}
        value={value}
        onChange={onChange}
        data={Object.values(ECastingType).map(action => { return { value: action, label: t("enum:" + action.toLowerCase()) }; })}
        {...rest}
    />;
}