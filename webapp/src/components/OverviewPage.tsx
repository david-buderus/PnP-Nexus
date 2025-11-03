import {useTranslation} from 'react-i18next';
import {useUniverseContext, useUserContext} from './PageBase';
import {Button, Group, Stack} from '@mantine/core';
import {HTMLPropsRef, MantineReactTable, type MRT_ColumnDef, useMantineReactTable,} from 'mantine-react-table';
import {useLocalStorage} from '@mantine/hooks';
import ConfirmationDialog from './modal/ConfirmationDialog';
import {AxiosResponse} from 'axios';
import {handleNetworkErrors} from './utils/ErrorUtils';
import {ReactNode} from 'react';

/**
 * An extended format of the column definition.
 * Including a hidden parameter.
 */
export interface ExtendedColumnDef<T, S> extends MRT_ColumnDef<T, S> {
    /** If the column should be hidden by default */
    defaultHidden?: boolean;
}

/** Props of the overview */
export interface OverviewPageProps<T> {
    /** A unique identifier for this overview */
    identifier: string;
    /** Callback to fetch data */
    fetchData: [T[], () => void, boolean];
    /** The columns */
    columns: ExtendedColumnDef<T, any>[];
    /** Callback to create dialogs to create or edit objects */
    manipulationDialog: (editMode: boolean, refresh: () => void, disabled: boolean, getInitial: () => T) => ReactNode;
    /** The title of the deletion dialog */
    deletionDialogTitle: string;
    /** Callback for the deletion */
    onDelete: (universe: string, objects: T[]) => Promise<AxiosResponse<void>>;
    /** The key to get the id of the object */
    idKey: keyof T;
}

export default function OverviewPage<T>({
    identifier,
    fetchData,
    columns,
    manipulationDialog,
    deletionDialogTitle,
    onDelete,
    idKey
}: OverviewPageProps<T>) {
    const {t} = useTranslation();
    const {activeUniverse} = useUniverseContext();
    const {userPermissions} = useUserContext();
    const [data, refresh, loading] = fetchData;

    const [visibility, setVisibility] = useLocalStorage<Record<string, boolean>>({
        key: identifier + '-visibility',
        defaultValue: columns.reduce<Record<string, boolean>>((rec, col) => {
            const key: string = col.id ?? (col.accessorKey) as string;
            rec[key] = !col.defaultHidden;
            return rec;
        }, {})
    });

    const table = useMantineReactTable({
        columns: columns as MRT_ColumnDef<T, any>[],
        data,
        state: {
            isLoading: loading,
            columnVisibility: visibility
        },
        onColumnVisibilityChange: setVisibility,
        enableRowSelection: true,
        mantineTableBodyRowProps: props => ({
            'data-testid': props.row.original[idKey]
        } as HTMLPropsRef<HTMLTableRowElement>),
    });

    return <Stack>
        <MantineReactTable
            data-testid="overview-table"
            table={table}
        />
        {userPermissions.canWriteActiveUniverse && <Group justify="flex-end">
            {manipulationDialog(false, refresh, false, () => undefined)}
            {manipulationDialog(true, refresh, table.getSelectedRowModel().flatRows.length !== 1, () => table.getSelectedRowModel().flatRows[0].original)}
            <ConfirmationDialog
                title={deletionDialogTitle}
                onConfirmation={() => onDelete(activeUniverse?.name, table.getSelectedRowModel().flatRows.map(row => row.original))
                    .then(refresh).catch(handleNetworkErrors)}
                openNode={(open) => <Button
                    data-testid="delete"
                    disabled={table.getSelectedRowModel().flatRows.length === 0}
                    onClick={open}
                >
                    {t('delete')}
                </Button>}
            />
        </Group>}
    </Stack>;
}