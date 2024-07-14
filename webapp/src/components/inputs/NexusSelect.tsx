import { FormControl, FormHelperText, InputLabel, MenuItem, Select, SelectProps, Tooltip } from "@mui/material";
import { ERarity } from "../../api";
import { useTranslation } from "react-i18next";
import { ReactNode } from "react";

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
    /** tooltip text */
    tooltip?: ReactNode;
}

/** A custom select with helpertext and error support */
export function NexusSelect<Value>(props: NexusSelectProps<Value>) {
    const { error, helperText, label, tooltip, fullWidth, value, values, ...rest } = props;

    return <FormControl fullWidth={fullWidth}>
        <InputLabel error={error}>{label}</InputLabel>
        <Tooltip title={tooltip} placement="right-start">
            <Select
                {...rest}
                fullWidth={fullWidth}
                value={value ?? ''}
                label={label}
                error={error}
            >
                {
                    values.map(v => <MenuItem key={v.key} value={v.content.toString()}> {v.label} </MenuItem>)
                }
            </Select>
        </Tooltip>
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
    const { value, onChange, error, ...rest } = props;
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

/** Props needed for the select */
interface LanguageSelectProps {
    /** The default value */
    language: string;
    /** On change handler */
    onChange: (language: string) => void;
    /** helper text */
    helperText?: string;
    /** indicates whether the selection is an error case */
    error?: boolean;
    /** if it fills the full width */
    fullWidth?: boolean;
}

/** A select for languages */
export function LanguageSelect(props: React.PropsWithChildren<LanguageSelectProps>) {
    const { t } = useTranslation();
    const { language, onChange, error, ...rest } = props;

    return <NexusSelect
        data-testid="language"
        label={t("language")}
        values={[
            {
                key: "de",
                content: "de",
                label: "Deutsch"
            },
            {
                key: "en",
                content: "en",
                label: "English"
            }
        ]}
        value={language}
        onChange={event => onChange(event.target.value)}
        sx={{ minWidth: 300 }}
        {...rest}
    />;
}