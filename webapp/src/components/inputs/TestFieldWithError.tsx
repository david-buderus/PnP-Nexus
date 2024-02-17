import { Tooltip } from "@mui/material";
import TextField, { TextFieldProps } from "@mui/material/TextField";
import { TFunction } from "i18next";
import { useState } from "react";
import { useTranslation } from "react-i18next";

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
    if (errorMap[fieldId]) {
        return {
            error: true,
            helperText: errorMap[fieldId]
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
    errorMap: Map<string, string>,
    /** If the textfield is a number field. */
    numberField?: boolean,
    /** If the textfield is an integer field. */
    integerField?: boolean;
    /** Tooltip for the textfield. */
    tooltip?: string;
} & Omit<TextFieldProps, 'variant' | 'onChange' | 'value'>) {
    const { fieldId, value, onChange, errorMap, numberField, integerField, tooltip, ...rest } = props;

    const { t } = useTranslation();

    return <Tooltip title={tooltip} placement="right-start">
        <TextField
            {...errorMessage(fieldId, errorMap, value, numberField, integerField, t)}
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
    errorMap: Map<string, string>,
    /** If the textfield is an integer field. */
    integerField?: boolean;
    /** Tooltip for the textfield. */
    tooltip?: string;
} & Omit<TextFieldProps, 'variant' | 'onChange' | 'value'>) {
    const { value, onChange, ...rest } = props;
    const [stringValue, setStringValue] = useState(Number.isNaN(value) ? "" : value.toString());

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
