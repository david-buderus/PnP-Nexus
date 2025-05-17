import { useTranslation } from "react-i18next";
import { useUniverseContext, useUserContext } from "./PageBase";
import { Button, Group, Stack } from "@mantine/core";
import {
    HTMLPropsRef,
    MantineReactTable,
    useMantineReactTable,
    type MRT_ColumnDef,
} from 'mantine-react-table';
import { useLocalStorage } from "@mantine/hooks";
import ConfirmationDialog from "./modal/ConfirmationDialog";
import { AxiosResponse } from "axios";
import { handleNetworkErrors } from "./utils/ErrorUtils";
import { ReactNode } from "react";

export interface ExtendedColumnDef<T, S> extends MRT_ColumnDef<T, S> {
    defaultHidden?: boolean;
}

export interface OverviewPageProps<T> {
    identifier: string;
    fetchData: [T[], () => void, boolean];
    columns: ExtendedColumnDef<T, any>[];
    manipulationDialog: (editMode: boolean, refresh: () => void, disabled: boolean, getInitial: () => T) => ReactNode;
    deletionDialogTitle: string;
    onDelete: (universe: string, objects: T[]) => Promise<AxiosResponse<void, any>>;
}

export default function OverviewPage<T>({
    identifier,
    fetchData,
    columns,
    manipulationDialog,
    deletionDialogTitle,
    onDelete
}: OverviewPageProps<T>) {
    const { t } = useTranslation();
    const { activeUniverse } = useUniverseContext();
    const { userPermissions } = useUserContext();
    const [data, refresh, loading] = fetchData;

    const [visibility, setVisibility] = useLocalStorage<Record<string, boolean>>({
        key: identifier + "-visibility",
        defaultValue: columns.reduce<Record<string, boolean>>((rec, col) => {
            const key: string = col.id ?? (col.accessorKey) as string;
            rec[key] = !col.defaultHidden;
            return rec;
        }, {})
    });

    const table = useMantineReactTable({
        columns: (columns as MRT_ColumnDef<T, any>[]),
        data,
        state: {
            isLoading: loading,
            columnVisibility: visibility
        },
        onColumnVisibilityChange: setVisibility,
        enableRowSelection: true,
        mantineTableBodyRowProps: (props) => ({
            'data-testid': props.row.original["id"]
        } as HTMLPropsRef<any>),
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
                onConfirmation={() => onDelete(activeUniverse.name, table.getSelectedRowModel().flatRows.map(row => row.original))
                    .then(refresh).catch(handleNetworkErrors)}
                openNode={(open) => <Button
                    data-testid="delete"
                    disabled={table.getSelectedRowModel().flatRows.length === 0}
                    onClick={open}
                >
                    {t("delete")}
                </Button>}
            />
        </Group>}
    </Stack>;
}