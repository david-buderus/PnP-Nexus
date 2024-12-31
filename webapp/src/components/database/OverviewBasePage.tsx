import { useTranslation } from "react-i18next";
import OverviewTable, { Column } from "../OverviewTable";
import { getUniverseContext, getUserContext } from "../PageBase";
import { useEffect, useState } from "react";
import { NoUniverse } from "../NoUniverse";
import { Button, Stack } from "@mui/material";
import { ConfirmationDialog } from "../inputs/ConfirmationDialog";
import { AxiosResponse } from "axios";
import { DatabaseObject, DatabaseObjectDialog, DatabaseObjectDialogField } from "./DatabaseObjectDialog";

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
    emptyObject: O & { "@type"?: string; },
    /** Fetches the database objects */
    fetchObjects: (universe: string) => Promise<AxiosResponse<O[], any>>;
    /** Edit the database objects */
    createObjects: (universe: string, databaseObjects: O[]) => Promise<AxiosResponse<O[], any>>;
    /** Edit the database objects */
    editObject: (universe: string, id: string, databaseObject: O) => Promise<AxiosResponse<O, any>>;
    /** Removes the database objects */
    removeObjects: (universe: string, keys: string[]) => Promise<AxiosResponse<void, any>>;
}

/**
 *  A page with a overview table and create, edit and delete functionallities.
 */
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

    return <div>
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
                    keyFormatter={key => {
                        // Prefix from repository service base
                        if (key.startsWith("insertAll.objects[0].")) {
                            return key.substring("insertAll.objects[0].".length);
                        }
                        return key;
                    }}
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
    </div>;
}
