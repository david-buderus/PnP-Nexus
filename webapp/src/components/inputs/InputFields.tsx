import { Autocomplete, Chip, Tooltip } from "@mui/material";
import TextField, { TextFieldProps } from "@mui/material/TextField";
import { TFunction } from "i18next";
import { ReactNode, useState } from "react";
import { useTranslation } from "react-i18next";
import { Dice, Tag } from "../../api";
import { diceFormatter } from "../Utils";

function errorMessage(fieldId: string, errorMap: Map<string, string>, value: string, numberField: boolean, integerField: boolean, t: TFunction<"translation", undefined>) {
    const n = Number(value);
    if (numberField && Number.isNaN(n)) {
        return {
            error: true,
            helperText: t('error:notNumber')
        };

    }
    if (integerField && !Number.isInteger(n)) {
        return {
            error: true,
            helperText: t('error:notInteger')
        };
    }
    if (errorMap?.get(fieldId)) {
        return {
            error: true,
            helperText: errorMap.get(fieldId)
        };
    }
    return {};
}

/**
 * A TextField where the helpertext is used as an error message.
 */
export function TextFieldWithError(props: {
    /** The id of the field which gets tested. Used as data-testid. */
    fieldId: string,
    /** The current value of the text field. */
    value: string,
    /** On change hook for the value. */
    onChange: (value: string) => void;
    /** All known errors. If the map contains the fieldId as key. The value will be shown as error. */
    errorMap?: Map<string, string>,
    /** If the textfield is a number field. */
    numberField?: boolean,
    /** If the textfield is an integer field. */
    integerField?: boolean;
    /** Tooltip for the textfield. */
    tooltip?: ReactNode;
} & Omit<TextFieldProps, 'variant' | 'onChange' | 'value'>) {
    const { fieldId, value, onChange, errorMap, numberField, integerField, tooltip, ...rest } = props;

    const { t } = useTranslation();

    return <Tooltip title={tooltip} placement="right-start" key={fieldId + "-tooltip"}>
        <TextField
            {...errorMessage(fieldId, errorMap, value, numberField, integerField, t)}
            key={fieldId}
            id={fieldId}
            data-testid={fieldId}
            variant="outlined"
            value={value}
            onChange={event => onChange(event.target.value)}
            {...rest}
        />
    </Tooltip>;
}

/**
 * A TextField where the helpertext is used as an error message.
 * 
 * Should be used as renderer in AutoCompletes.
 */
export function TextFieldWithErrorForAutoComplete(props: {
    /** The id of the field which gets tested. Used as data-testid. */
    fieldId: string,
    /** All known errors. If the map contains the fieldId as key. The value will be shown as error. */
    errorMap: Map<string, string>;
} & Omit<TextFieldProps, 'variant'>) {
    const { fieldId, errorMap, ...rest } = props;

    const { t } = useTranslation();

    return <TextField
        {...errorMessage(fieldId, errorMap, null, false, false, t)}
        key={fieldId}
        id={fieldId}
        variant="outlined"
        {...rest}
    />;
}

/**
 * A NumberField where the helpertext is used as an error message.
 */
export function NumberFieldWithError(props: {
    /** The id of the field which gets tested. Used as data-testid. */
    fieldId: string,
    /** The current value of the text field. */
    value: number,
    /** On change hook for the value. */
    onChange: (value: number) => void;
    /** All known errors. If the map contains the fieldId as key. The value will be shown as error. */
    errorMap?: Map<string, string>,
    /** If the textfield is an integer field. */
    integerField?: boolean;
    /** Tooltip for the textfield. */
    tooltip?: ReactNode;
} & Omit<TextFieldProps, 'variant' | 'onChange' | 'value'>) {
    const { value, onChange, ...rest } = props;
    const [stringValue, setStringValue] = useState(Number.isNaN(value) ? "" : value === undefined ? "" : value.toString());

    return <TextFieldWithError
        value={stringValue}
        onChange={newValue => {
            setStringValue(newValue);
            onChange(Number(newValue));
        }}
        numberField
        {...rest}
    />;
}

export function TagListField(props: {
    /** The id of the field which gets tested. Used as data-testid. */
    fieldId: string,
    /** The current value of the text field. */
    value: Tag[],
    /** On change hook for the value. */
    onChange: (value: Tag[]) => void;
    /** All known tags */
    options: Tag[];
    /** All known errors. If the map contains the fieldId as key. The value will be shown as error. */
    errorMap?: Map<string, string>,
    /** Tooltip for the textfield. */
    tooltip?: ReactNode;
} & Omit<TextFieldProps, 'variant' | 'onChange' | 'value'>) {
    const { fieldId, value, onChange, options, errorMap, ...rest } = props;
    const { t } = useTranslation();

    return <Autocomplete
        {...errorMessage(fieldId, errorMap, null, false, false, t)}
        key={fieldId}
        multiple
        freeSolo
        fullWidth
        options={options?.map(t => t.name)}
        value={value?.map(t => t.name)}
        onChange={(_, v) => onChange(v.map(s => { return { name: s } as Tag; }))}
        renderTags={(value: readonly string[], getTagProps) =>
            value.map((option: string, index: number) => {
                const { key, ...tagProps } = getTagProps({ index });
                return (
                    <Chip variant="outlined" label={option} key={key} {...tagProps} />
                );
            })
        }
        renderInput={(params) => (
            <TextFieldWithErrorForAutoComplete
                {...params}
                fieldId={fieldId}
                errorMap={errorMap}
                {...rest}
            />
        )}
    />;
}

export function StringListField(props: {
    /** The id of the field which gets tested. Used as data-testid. */
    fieldId: string,
    /** The current value of the text field. */
    value: string[],
    /** On change hook for the value. */
    onChange: (value: string[]) => void;
    /** All known errors. If the map contains the fieldId as key. The value will be shown as error. */
    errorMap?: Map<string, string>,
    /** Tooltip for the textfield. */
    tooltip?: ReactNode;
} & Omit<TextFieldProps, 'variant' | 'onChange' | 'value'>) {
    const { value, onChange, ...rest } = props;
    const [stringValue, setStringValue] = useState(value === undefined ? "" : Array.from(value).join(", "));

    return <TextFieldWithError
        value={stringValue}
        onChange={newValue => {
            setStringValue(newValue);
            onChange(newValue.split(",").map(s => s.trim()).filter(s => s));
        }}
        {...rest}
    />;
}

/**
 * A textfield where the string gets parsed into a dice.
 * 
 * The value is null if the string is not a valid dice.
 */
export function DiceField(props: {
    /** The id of the field which gets tested. Used as data-testid. */
    fieldId: string,
    /** The current value of the text field. */
    value: Dice,
    /** On change hook for the value. */
    onChange: (value: Dice) => void;
    /** All known errors. If the map contains the fieldId as key. The value will be shown as error. */
    errorMap?: Map<string, string>,
    /** Tooltip for the textfield. */
    tooltip?: ReactNode;
} & Omit<TextFieldProps, 'variant' | 'onChange' | 'value'>) {
    const { value, onChange, ...rest } = props;
    const [stringValue, setStringValue] = useState(value === undefined ? "" : diceFormatter(value));
    const [validDice, setValidDice] = useState(true);

    return <TextFieldWithError
        value={stringValue}
        onChange={newValue => {
            setStringValue(newValue);

            const newDice = parseDice(newValue);
            onChange(newDice);
            setValidDice(newDice !== null);
        }}
        {...rest}
        {...diceError(validDice)}
    />;
}

function diceError(validDice: boolean) {
    const { t } = useTranslation();

    if (validDice) {
        return {};
    }
    return {
        helperText: t("error:notDice"),
        error: true
    };
}

const IS_DICE = /^\s*([1-9][0-9]*)?\s*D[1-9][0-9]*(\s*\+\s*([1-9][0-9]*)?\s*D[1-9][0-9]*)*\s*$/;

function parseDice(s: string): Dice {
    if (!s) {
        return {
            dices: []
        };
    }
    if (!IS_DICE.test(s)) {
        return null;
    }

    return {
        dices: s.split("+").map(d => d.split("D").map(d => d.trim())).map(d => {
            return {
                numberOfThrows: d[0] ? Number(d[0]) : 1,
                dice: Number(d[1])
            };
        })
    };
}