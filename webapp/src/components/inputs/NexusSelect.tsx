import { FormControl, FormHelperText, InputLabel, MenuItem, Select, SelectProps } from "@mui/material";
import { ERarity } from "../../api";
import { useTranslation } from "react-i18next";

/** Props needed for the select */
interface NexusSelectProps<Value> extends SelectProps<Value> {
    /** label of the select */
    label: string;
    /** helper text */
    helperText?: string;
    /** indicates whether the selection is an error case */
    error?: boolean;
    /** all possible values of the select */
    values: {
        key?: string;
        content: Value;
        label: string;
    }[];
}

/** A custom select with helpertext and error support */
export function NexusSelect<Value>(props: NexusSelectProps<Value>) {
    const { error, helperText, label, fullWidth, value, values, ...rest } = props;

    return <FormControl fullWidth={fullWidth}>
        <InputLabel error={error}>{label}</InputLabel>
        <Select
            {...rest}
            fullWidth={fullWidth}
            value={value ?? ''}
            label={label}
            error={error}
        >
            {
                values.map(value => <MenuItem key={value.key} value={value.content.toString()}> {value.label} </MenuItem>)
            }
        </Select>
        {helperText !== undefined && <FormHelperText error={error}>{helperText}</FormHelperText>}
    </FormControl>;
}

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
    fullWidth?: boolean;
}

/** A select for rarity */
export function RaritySelect(props: React.PropsWithChildren<RaritySelectProps>) {
    const { value, onChange, helperText, error, ...rest } = props;
    const { t } = useTranslation();

    return <NexusSelect
        label={t("rarity")}
        value={value}
        onChange={event => { onChange(event.target.value as ERarity); }}
        error={error}
        values={Object.values(ERarity).map(rarity => { return { key: rarity, content: rarity, label: t("enum:" + rarity.toLowerCase()) }; })}
        {...rest}
    />;
}