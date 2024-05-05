import { useTranslation } from "react-i18next";
import OverviewTable, { Column } from "./OverviewTable";
import { getUniverseContext, getUserContext } from "./PageBase";
import { useEffect, useState } from "react";
import { NoUniverse } from "./NoUniverse";
import { Autocomplete, Button, Dialog, DialogActions, DialogTitle, Stack, Tooltip } from "@mui/material";
import { ConfirmationDialog } from "./inputs/ConfirmationDialog";
import { AxiosResponse } from "axios";
import { handleValidationError } from "./ErrorUtils";
import { NumberFieldWithError, TextFieldWithError, TextFieldWithErrorForAutoComplete } from "./inputs/TestFieldWithError";
import { NexusSelect } from "./inputs/NexusSelect";
import { FaMinus, FaPlus } from "react-icons/fa";

interface DatabaseObject {
    id?: string;
}

/** Props needed for the items page */
export interface OverviewBasePageProps<O extends DatabaseObject> {
    /** The columns shown on the item page */
    columns: Column<O>[];
    /** Default key used for sorting */
    sortingKey: keyof O;
    /** Entry field of the database object */
    fields: DatabaseObjectDialogField<O, any>[];
    /** Deletion dialog title */
    creationDialogTitle: string;
    /** Deletion dialog title */
    editDialogTitle: string;
    /** Deletion dialog title */
    deletionDialogTitle: string;
    /** An empty object */
    emptyObject: O,
    /** Fetches the database objects */
    fetchObjects: (universe: string) => Promise<AxiosResponse<O[], any>>;
    /** Edit the database objects */
    createObjects: (universe: string, databaseObjects: O[]) => Promise<AxiosResponse<O[], any>>;
    /** Edit the database objects */
    editObject: (universe: string, id: string, databaseObject: O) => Promise<AxiosResponse<O, any>>;
    /** Removes the database objects */
    removeObjects: (universe: string, keys: string[]) => Promise<AxiosResponse<void, any>>;
}

export function OverviewBasePage<O extends DatabaseObject>({
    columns,
    sortingKey,
    fields,
    creationDialogTitle,
    editDialogTitle,
    deletionDialogTitle,
    emptyObject,
    fetchObjects,
    createObjects,
    editObject,
    removeObjects
}: OverviewBasePageProps<O>) {
    const { t } = useTranslation();
    const { activeUniverse } = getUniverseContext();
    const { userPermissions } = getUserContext();

    const [databaseObjects, setDatabaseObjects] = useState<O[]>([]);
    const [selected, setSelected] = useState<O[keyof O][]>([]);

    const [openCreationDialog, setOpenCreationDialog] = useState(false);
    const [openEditDialog, setOpenEditDialog] = useState(false);
    const [openDeleteDialog, setOpenDeleteDialog] = useState(false);

    useEffect(() => {
        if (activeUniverse === null) {
            return;
        }
        fetchObjects(activeUniverse.name).then(response => setDatabaseObjects(response.data));
    }, [activeUniverse]);


    if (!activeUniverse) {
        return <NoUniverse />;
    }

    return <Stack spacing={2} padding={2}>
        <OverviewTable id='id' sortBy={sortingKey} data={databaseObjects} columns={columns} selectedState={[selected, setSelected]} />
        {
            userPermissions.canWriteActiveUniverse &&
            <Stack spacing={2} direction="row" justifyContent="flex-end">
                <Button className='btn' data-testid="add" onClick={() => setOpenCreationDialog(true)}>
                    {t("add")}
                </Button>
                <Button className='btn' data-testid="edit" disabled={selected.length !== 1} onClick={() => setOpenEditDialog(true)}>
                    {t("edit")}
                </Button>
                <Button className='btn' data-testid="delete" disabled={selected.length === 0} onClick={() => setOpenDeleteDialog(true)}>
                    {t("delete")}
                </Button>
                <DatabaseObjectDialog<O>
                    open={openCreationDialog}
                    onClose={(_, reason) => {
                        if (reason === "successful") {
                            fetchObjects(activeUniverse.name).then(response => setDatabaseObjects(response.data));
                        }
                        setOpenCreationDialog(false);
                    }}
                    onAction={(universe, obj) => createObjects(universe, [obj])}
                    keyFormatter={key => key.substring(key.indexOf("[0].") + 4)}
                    initalObject={emptyObject}
                    fields={fields}
                    title={creationDialogTitle}
                    actionButtonText={t("create")}
                />
                <DatabaseObjectDialog<O>
                    key={openEditDialog ? selected[0] as string : "no-selection"}
                    open={openEditDialog}
                    onClose={(_, reason) => {
                        if (reason === "successful") {
                            fetchObjects(activeUniverse.name).then(response => setDatabaseObjects(response.data));
                        }
                        setOpenEditDialog(false);
                    }}
                    onAction={(universe, obj) => editObject(universe, obj.id, obj)}
                    initalObject={openEditDialog ? databaseObjects.find(obj => obj.id === selected[0]) : null}
                    fields={fields}
                    title={editDialogTitle}
                    actionButtonText={t("edit")}
                />
                <ConfirmationDialog
                    title={deletionDialogTitle}
                    open={openDeleteDialog}
                    onClose={confirmation => {
                        setOpenDeleteDialog(false);
                        if (!confirmation) {
                            return;
                        }
                        removeObjects(activeUniverse.name, selected as string[]).then(sucessful => {
                            if (sucessful.status < 400) {
                                fetchObjects(activeUniverse.name).then(response => setDatabaseObjects(response.data));
                            }
                        });
                    }}
                />
            </Stack>
        }
    </Stack>;
};

interface DatabaseObjectDialogField<O extends DatabaseObject, D> {
    fieldId: keyof O;
    fullId?: string;
    label: string;
    fieldType: "STRING" | "NUMBER" | "ENUM" | "DATABASE" | "COMPLEX_ENTRY" | "COMPLEX_LIST";
    dependency?: D[];
    dependencyLabel?: keyof D;
    subFields?: DatabaseObjectDialogField<any, any>[];
    emptyObject?: any;
    newListObjectLabel?: string;
}

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

function DatabaseObjectDialog<O extends DatabaseObject>({
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

    return <Dialog open={open} onClose={onClose} fullWidth data-testid="database-object-dialog"
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
                />
            )}
        </Stack>
        <DialogActions>
            <Button data-testid="dialog-cancel" autoFocus onClick={() => onClose({}, "cancel")}>
                {t('cancel')}
            </Button>
            <Button data-testid="dialog-action" onClick={() => {
                onAction(activeUniverse.name, databaseObject).then(() => onClose({}, "successful")).catch(handleValidationError(setErrors, keyFormatter));
            }}>{actionButtonText}</Button>
        </DialogActions>
    </Dialog>;
}

interface FieldProps<O extends DatabaseObject, D extends DatabaseObject> {
    /** Entry field of the database object */
    field: DatabaseObjectDialogField<O, D>;
    /** The current errors */
    errors: Map<string, string>;
    databaseObject: O;
    setDatabaseObject: (obj: O) => void;
}

function Field<O extends DatabaseObject, D extends DatabaseObject>({
    field,
    errors,
    databaseObject,
    setDatabaseObject
}: FieldProps<O, D>) {
    const { t } = useTranslation();

    const fullId = (field.fullId ?? field.fieldId) as string;

    switch (field.fieldType) {
        case "STRING":
            return <TextFieldWithError
                key={fullId}
                fieldId={fullId}
                label={field.label}
                value={databaseObject?.[field.fieldId] as string}
                onChange={value => setDatabaseObject({
                    ...databaseObject,
                    [field.fieldId]: value
                })}
                errorMap={errors}
            />;
        case "NUMBER":
            return <NumberFieldWithError
                key={fullId}
                fieldId={fullId}
                label={field.label}
                value={databaseObject?.[field.fieldId] as number}
                onChange={value => setDatabaseObject({
                    ...databaseObject,
                    [field.fieldId]: value
                })}
                errorMap={errors}
            />;
        case "ENUM":
            return <NexusSelect<D>
                key={fullId}
                label={field.label}
                values={field.dependency as any}
                value={databaseObject?.[field.fieldId] as any}
                onChange={event => setDatabaseObject({
                    ...databaseObject,
                    [field.fieldId]: event.target.value
                })}
            />;
        case "DATABASE":
            return <Autocomplete
                key={fullId}
                fullWidth
                disablePortal
                options={field.dependency}
                getOptionLabel={(option: D) => {
                    return option?.[field.dependencyLabel] as string;
                }}
                isOptionEqualToValue={(option: DatabaseObject, value: DatabaseObject) => option.id === value.id}
                renderInput={(params) => <TextFieldWithErrorForAutoComplete {...params} fieldId={fullId} errorMap={errors} label={field.label} />}
                value={databaseObject?.[field.fieldId] ?? null}
                onChange={(_, value) => setDatabaseObject({
                    ...databaseObject,
                    [field.fieldId]: value
                })}
                data-testid={field.fieldId}
            />;
        case "COMPLEX_ENTRY":
            return <Stack direction="row" spacing={2} alignItems="flex-start" key={fullId + "-stack"} >
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
                        databaseObject={databaseObject[field.fieldId]}
                        setDatabaseObject={obj => {
                            setDatabaseObject({
                                ...databaseObject,
                                [field.fieldId]: obj

                            });
                        }}
                    />;
                })}
            </Stack>;
        case "COMPLEX_LIST":
            const entries = databaseObject[field.fieldId] as any[];
            return <Stack spacing={2} key={fullId + "-stack"} >
                {entries.map((value, index) => {
                    const fieldIdPrefix = fullId + "[" + index + "].";

                    return <Stack direction="row" spacing={2} alignItems="flex-start" key={fullId + "-stack-" + index} >
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
                        <Button key={fullId + "-sub-" + index} onClick={() => {
                            setDatabaseObject({
                                ...databaseObject,
                                [field.fieldId]: entries.filter((_, i) => i !== index)
                            });
                        }} sx={{ width: 1 / 4, paddingTop: 1.5 }} > <FaMinus size={20} /> </Button>
                    </Stack>;
                })}
                <Tooltip title={errors.get(fullId)}>
                    <Button
                        key={fullId + "-add"}
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
            </Stack>;
    }
}