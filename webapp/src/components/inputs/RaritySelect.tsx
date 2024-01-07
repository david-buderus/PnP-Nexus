import { FormControl, FormHelperText, InputLabel, MenuItem, Select } from "@mui/material";
import { ERarity } from "../../api";
import { useTranslation } from "react-i18next";
import React from "react";

/** Props needed for the select */
interface RaritySelectProps {
    /** The default value */
    value: ERarity;
    /** On change handler */
    onChange: (rarity: ERarity) => void;
    /** helper text */
    helperText?: string;
    /** indicates whether the selection is an error case */
    error?: boolean;
    /** if it fills the full width */
    fullWidth?: boolean
}

/** A select for rarity */
export function RaritySelect(props: React.PropsWithChildren<RaritySelectProps>) {
    const { value, onChange, helperText, error, ...rest} = props;
    const { t } = useTranslation();

    return <FormControl fullWidth>
        <InputLabel id="rarity-label" error={error}>{t("rarity")}</InputLabel>
        <Select
            {...rest}
            value={value ?? ''}
            label={t("rarity")}
            onChange={event => { onChange(event.target.value as ERarity); }}
            error={error}
        >
            {
                Object.values(ERarity).map(rarity => <MenuItem key={rarity} value={rarity}>{t("enum:" + rarity.toLowerCase())}</MenuItem>)
            }
        </Select>
        { helperText !== undefined ? <FormHelperText error={error}>{helperText}</FormHelperText> : '' }
    </FormControl>;
}
