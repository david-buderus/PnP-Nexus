import { useTranslation } from "react-i18next";
import { getUniverseContext } from '../PageBase';
import { ReactNode, useState } from "react";
import { Autocomplete, Button, Checkbox, Dialog, DialogActions, DialogTitle, FormControlLabel, FormGroup, Paper, Stack, TextField, Tooltip, Typography } from "@mui/material";
import { AxiosResponse } from "axios";
import { handleValidationErrors } from "../ErrorUtils";
import { DiceField, NumberFieldWithError, StringListField, TagListField, TextFieldWithError, TextFieldWithErrorForAutoComplete } from "../inputs/InputFields";
import { NexusSelect } from "../inputs/NexusSelect";
import { FaMinus, FaPlus } from "react-icons/fa";
import { currencyToHumanReadable } from "../Utils";
import { Dice, Tag } from "../../api";

/**
 * A simple interface to describe database objects
 */
export interface DatabaseObject {
    /** The unique identifier of a database object */
    id?: string;
}

/**
 * Describes an input field of a propery
 */
export interface DatabaseObjectDialogField<O extends DatabaseObject, D> {
    /** The id of the property */
    fieldId: keyof O | "@type" | `${string}-row`;
    /** The full id of the property. This includes the id of the parent object if one exists */
    fullId?: string;
    /** The label used for the input */
    label: string;
    /** The tooltip for the input */
    tooltip?: ReactNode;
    /** What kind of field is needed */
    fieldType: "STRING" | "NUMBER" | "BOOLEAN" | "DICE" | "PRICE" | "ENUM" | "DATABASE" | "MULTI_DATABASE" | "COMPLEX_ENTRY" | "COMPLEX_LIST" | "STACK" | "TAG_LIST";
    /** The dependencies needed for the field. Needed for ENUM, DATABASE and MULTI_DATABASE */
    dependency?: D[];
    /** The label used for the dependency */
    dependencyLabel?: keyof D;
    /** Fields that needed to describe the property. Needed for COMPLEX_ENTRY, COMPLEX_LIST, STACK */
    subFields?: DatabaseObjectDialogField<any, any>[];
    /** How a new object looks like this entry. NEEDED for COMPLEX_LIST */
    emptyObject?: any;
    /** The label of the button to add a new object to the COMPLEX_LIST */
    newListObjectLabel?: string;
    /** How the field should be aligment */
    aligment?: "row" | "column";
    /** Only shows the input if the @type is contained in the list */
    visibleForTypes?: string[];
    /** If the field should be a multiline field */
    multiline?: boolean;
    /** If the field should be hidden */
    hidden?: boolean;
    directField?: boolean;
}

/**
 * Props needed for the dialog
 */
interface DatabaseObjectDialogProps<O extends DatabaseObject> {
    /** If the dialog is open */
    open: boolean;
    /** On close handler */
    onClose: (event: unknown, reason: "backdropClick" | "escapeKeyDown" | "successful" | "cancel") => void;
    /** Event when the action button is clicked */
    onAction: (universe: string, databaseObject: O) => Promise<AxiosResponse<O | O[], any>>;
    /** the key formatter used for error on the action */
    keyFormatter?: (key: string) => string;
    /** Entry field of the database object */
    fields: DatabaseObjectDialogField<O, any>[];
    /** Initial object */
    initalObject: O;
    /** title of the dialog */
    title: string;
    /** action button text */
    actionButtonText: string;
}

/** A dialog to create or edit any database object */
export function DatabaseObjectDialog<O extends DatabaseObject>({
    open,
    onClose,
    onAction,
    keyFormatter,
    fields,
    initalObject,
    title,
    actionButtonText
}: DatabaseObjectDialogProps<O>) {
    const { t } = useTranslation();
    const { activeUniverse } = getUniverseContext();

    const [errors, setErrors] = useState<Map<string, string>>(new Map<string, string>());
    const [databaseObject, setDatabaseObject] = useState<O>(initalObject);

    return <Dialog
        open={open}
        onClose={onClose}
        fullWidth
        data-testid="database-object-dialog"
    >
        <DialogTitle>{title}</DialogTitle>
        <Stack spacing={2} padding={2}>
            {fields.map(field =>
                <Field
                    key={(field.fieldId as string) + "-field"}
                    field={field}
                    databaseObject={databaseObject}
                    setDatabaseObject={setDatabaseObject}
                    errors={errors}
                    multiline={field.multiline}
                />
            )}
        </Stack>
        <DialogActions>
            <Button data-testid="dialog-cancel" autoFocus onClick={() => onClose({}, "cancel")}>
                {t('cancel')}
            </Button>
            <Button data-testid="dialog-action" onClick={() => {
                onAction(activeUniverse.name, databaseObject).then(() => onClose({}, "successful")).catch(handleValidationErrors(setErrors, keyFormatter));
            }}>{actionButtonText}</Button>
        </DialogActions>
    </Dialog>;
}

/** Props needed to create a field */
interface FieldProps<O, D> {
    /** Entry field of the database object */
    field: DatabaseObjectDialogField<O, D>;
    /** The current errors */
    errors: Map<string, string>;
    /** The current state of the database object */
    databaseObject: O;
    /** Callback to manipulate the database object */
    setDatabaseObject: (obj: O) => void;
    /** If the field should be a multiline field */
    multiline?: boolean;
}

/** Creates a field */
export function Field<O, D>({
    field,
    errors,
    databaseObject,
    setDatabaseObject,
    multiline
}: FieldProps<O, D>): React.JSX.Element {
    const { t } = useTranslation();
    const { currencySettings } = getUniverseContext();

    const fullId = (field.fullId ?? field.fieldId) as string;
    const keyId = field.fieldId as keyof O;

    if (field.hidden) {
        return <></>;
    }

    if (field.visibleForTypes !== undefined && !field.visibleForTypes.includes(databaseObject["@type"])) {
        return <></>;
    }

    switch (field.fieldType) {
        case "STRING":
            return <TextFieldWithError
                key={fullId}
                fieldId={fullId}
                label={field.label}
                tooltip={field.tooltip}
                value={databaseObject?.[keyId] as string}
                onChange={value => setDatabaseObject({
                    ...databaseObject,
                    [field.fieldId]: value
                })}
                errorMap={errors}
                fullWidth
                multiline={multiline}
                rows={2}
            />;
        case "TAG_LIST":
            return <TagListField
                key={fullId}
                fieldId={fullId}
                label={field.label}
                tooltip={field.tooltip}
                value={databaseObject?.[keyId] as Tag[]}
                onChange={value => {
                    if (field.directField) {
                        setDatabaseObject(value as O);
                    } else {
                        setDatabaseObject({
                            ...databaseObject,
                            [field.fieldId]: value
                        });
                    }
                }}
                options={field.dependency as Tag[]}
                errorMap={errors}
                fullWidth
                multiline={multiline}
                rows={2}
            />;
        case "NUMBER":
            return <NumberFieldWithError
                key={fullId}
                fieldId={fullId}
                label={field.label}
                tooltip={field.tooltip}
                value={databaseObject?.[keyId] as number}
                onChange={value => setDatabaseObject({
                    ...databaseObject,
                    [field.fieldId]: value
                })}
                errorMap={errors}
                fullWidth
            />;
        case "BOOLEAN":
            return <FormGroup key={fullId + "-group"}>
                <FormControlLabel
                    key={fullId}
                    control={
                        <Tooltip title={field.tooltip} placement="right-start">
                            <Checkbox
                                data-testid={fullId}
                                checked={databaseObject?.[keyId] as boolean}
                                onChange={event => setDatabaseObject({
                                    ...databaseObject,
                                    [field.fieldId]: event.target.checked
                                })}
                            />
                        </Tooltip>
                    }
                    label={field.label}
                />
            </FormGroup>;
        case "DICE":
            return <DiceField
                key={fullId}
                fieldId={fullId}
                label={field.label}
                tooltip={field.tooltip}
                value={databaseObject?.[keyId] as Dice}
                onChange={value => setDatabaseObject({
                    ...databaseObject,
                    [field.fieldId]: value
                })}
                errorMap={errors}
                fullWidth
            />;
        case "PRICE":
            return <Stack direction="row" spacing={2}>
                <NumberFieldWithError
                    key={fullId}
                    fieldId={fullId}
                    errorMap={errors}
                    integerField
                    label={field.label}
                    tooltip={field.tooltip}
                    value={databaseObject?.[keyId] as number}
                    onChange={value => setDatabaseObject({
                        ...databaseObject,
                        [field.fieldId]: value
                    })}
                    fullWidth
                />
                <TextField
                    key={fullId + "-resulting"}
                    label={t("resultingPrice")}
                    data-testid="resultingPrice"
                    variant="outlined"
                    value={currencyToHumanReadable(currencySettings, Number(databaseObject?.[keyId]))}
                    InputProps={{ readOnly: true }}
                    fullWidth
                />
            </Stack>;
        case "ENUM":
            return <NexusSelect
                key={fullId}
                data-testid={fullId}
                label={field.label}
                tooltip={field.tooltip}
                values={field.dependency as any}
                value={databaseObject?.[keyId] as any}
                onChange={event => setDatabaseObject({
                    ...databaseObject,
                    [field.fieldId]: event.target.value
                })}
                fullWidth
                error={errors.has(fullId)}
                helperText={errors.get(fullId)}
            />;
        case "DATABASE":
            return <Tooltip title={field.tooltip} placement="right-start">
                <Autocomplete
                    key={fullId}
                    options={field.dependency}
                    getOptionLabel={(option: D) => {
                        return option?.[field.dependencyLabel] as string;
                    }}
                    isOptionEqualToValue={(option: DatabaseObject, value: DatabaseObject) => option.id === value.id}
                    renderInput={(params) => <TextFieldWithErrorForAutoComplete {...params} fieldId={fullId} errorMap={errors} label={field.label} />}
                    value={databaseObject?.[keyId] as D ?? null}
                    onChange={(_, value) => setDatabaseObject({
                        ...databaseObject,
                        [field.fieldId]: value
                    })}
                    data-testid={fullId}
                    fullWidth
                />
            </Tooltip>;
        case "MULTI_DATABASE":
            return <Tooltip title={field.tooltip} placement="right-start">
                <Autocomplete
                    key={fullId}
                    multiple
                    options={field.dependency}
                    getOptionLabel={(option: D) => {
                        return option?.[field.dependencyLabel] as string;
                    }}
                    isOptionEqualToValue={(option: DatabaseObject, value: DatabaseObject) => option?.id === value?.id}
                    renderInput={(params) => <TextFieldWithErrorForAutoComplete {...params} fieldId={fullId} errorMap={errors} label={field.label} />}
                    value={databaseObject?.[keyId] as D[] ?? []}
                    onChange={(_, value) => setDatabaseObject({
                        ...databaseObject,
                        [field.fieldId]: value
                    })}
                    data-testid={fullId}
                    fullWidth
                />
            </Tooltip>;
        case "COMPLEX_ENTRY":
            return <Stack direction={field.aligment ?? "row"} spacing={2} key={fullId} data-testid={fullId}>
                {field.subFields!.map(subField => {
                    const subFullId = fullId + "." + (subField.fieldId as string);

                    return <Field
                        key={subFullId + "-field"}
                        field={{
                            ...subField,
                            fieldId: subField.fieldId as any,
                            fullId: subFullId
                        }}
                        errors={errors}
                        databaseObject={databaseObject[keyId]}
                        setDatabaseObject={obj => {
                            setDatabaseObject({
                                ...databaseObject,
                                [field.fieldId]: obj

                            });
                        }}
                    />;
                })}
            </Stack>;
        case "STACK": {
            let idPrefix = fullId.substring(0, fullId.length - field.fieldId.toString().length);
            if (idPrefix === ".") {
                idPrefix = "";
            }
            return <Stack direction="row" spacing={2} alignItems="flex-startline" key={fullId + "-stack"}>
                {field.subFields!.map(subField => {
                    return <Field
                        key={fullId + "." + (subField.fieldId as string) + "-field"}
                        field={{
                            ...subField,
                            fieldId: subField.fieldId as keyof O,
                            fullId: idPrefix + (subField.fieldId as string)
                        }}
                        errors={errors}
                        databaseObject={databaseObject}
                        setDatabaseObject={obj => {
                            setDatabaseObject(obj);
                        }}
                    />;
                })}
            </Stack>;
        }
        case "COMPLEX_LIST":
            return <ComplexFieldList
                field={field}
                errors={errors}
                databaseObject={databaseObject}
                setDatabaseObject={setDatabaseObject}
            />;
        default:
            return <>Unknown Fieldtype</>;
    }
}

function ComplexFieldList<O extends DatabaseObject, D extends DatabaseObject>({
    field,
    errors,
    databaseObject,
    setDatabaseObject
}: FieldProps<O, D>) {
    const fullId = (field.fullId ?? field.fieldId) as string;
    const entries = databaseObject[field.fieldId as keyof O] as any[];

    return <Paper className='p-4'>
        <Stack spacing={2} key={fullId} data-testid={fullId} >
            <Tooltip title={field.tooltip}>
                <Typography variant="subtitle1">
                    {field.label}
                </Typography>
            </Tooltip>
            {entries.map((value, index) => {
                const fieldIdPrefix = fullId + "[" + index + "].";

                if (field.aligment === "column") {
                    const lastSubField = field.subFields[field.subFields.length - 1];

                    return <Stack direction="column" spacing={2} key={fullId + "-stack-" + index} >
                        {field.subFields!.slice(0, -1).map(subField => {
                            const subFullId = fieldIdPrefix + (subField.fieldId as string);

                            return <Field
                                key={subFullId + "-field"}
                                field={{
                                    ...subField,
                                    fullId: subFullId
                                }}
                                errors={errors}
                                databaseObject={value}
                                setDatabaseObject={obj => {
                                    setDatabaseObject({
                                        ...databaseObject,
                                        [field.fieldId]: entries.map((e, i) => index !== i ? e : obj)

                                    });
                                }}
                            />;
                        })}
                        <Stack direction="row" spacing={2} alignItems="flex-startline" key={fullId + "-button-stack-" + index}>
                            <Field
                                key={fieldIdPrefix + (lastSubField.fieldId as string) + "-field"}
                                field={{
                                    ...lastSubField,
                                    fullId: fieldIdPrefix + (lastSubField.fieldId as string)
                                }}
                                errors={errors}
                                databaseObject={value}
                                setDatabaseObject={obj => {
                                    setDatabaseObject({
                                        ...databaseObject,
                                        [field.fieldId]: entries.map((e, i) => index !== i ? e : obj)

                                    });
                                }}
                            />
                            <Button
                                key={fullId + "-sub-" + index}
                                data-testid={fullId + "-sub-" + index}
                                onClick={() => {
                                    setDatabaseObject({
                                        ...databaseObject,
                                        [field.fieldId]: entries.filter((_, i) => i !== index)
                                    });
                                }}
                                sx={{ width: 1 / 4, paddingTop: 1.5 }}
                            >
                                <FaMinus size={20} />
                            </Button>
                        </Stack>
                    </Stack>;
                } else {
                    return <Stack direction="row" spacing={2} alignItems="flex-startline" key={fullId + "-stack-" + index} >
                        {field.subFields!.map(subField => {
                            const subFullId = fieldIdPrefix + (subField.fieldId as string);

                            return <Field
                                key={subFullId + "-field"}
                                field={{
                                    ...subField,
                                    fullId: subFullId
                                }}
                                errors={errors}
                                databaseObject={value}
                                setDatabaseObject={obj => {
                                    setDatabaseObject({
                                        ...databaseObject,
                                        [field.fieldId]: entries.map((e, i) => index !== i ? e : obj)

                                    });
                                }}
                            />;
                        })}
                        <Button
                            key={fullId + "-sub-" + index}
                            data-testid={fullId + "-sub-" + index}
                            onClick={() => {
                                setDatabaseObject({
                                    ...databaseObject,
                                    [field.fieldId]: entries.filter((_, i) => i !== index)
                                });
                            }} sx={{ width: 1 / 4, paddingTop: 1.5 }} > <FaMinus size={20} /> </Button>
                    </Stack>;
                }
            })}
            <Tooltip title={errors.get(fullId)}>
                <Button
                    key={fullId + "-add"}
                    data-testid={fullId + "-add"}
                    fullWidth
                    startIcon={<FaPlus />}
                    color={errors.get(fullId) ? "error" : "primary"}
                    onClick={() => setDatabaseObject({
                        ...databaseObject,
                        [field.fieldId]: entries.concat([field.emptyObject])
                    })}>
                    {field.newListObjectLabel}
                </Button>
            </Tooltip>
        </Stack>
    </Paper>;
}