import {useTranslation} from 'react-i18next';
import {useUniverseContext, useUserContext} from './PageBase';
import {Box, Button, Checkbox, Group, Menu, Paper, rem, Stack, Table, Text, TextInput} from '@mantine/core';
import {useLocalStorage} from '@mantine/hooks';
import ConfirmationDialog from './modal/ConfirmationDialog';
import {AxiosResponse} from 'axios';
import {handleNetworkErrors} from './utils/ErrorUtils';
import React, {ReactNode, useState} from 'react';
import {
    Column,
    flexRender,
    getCoreRowModel,
    getFilteredRowModel,
    getSortedRowModel,
    SortingState,
    useReactTable
} from '@tanstack/react-table';
import {ExtendedColumnDef} from './table/SortableTable';
import {
    IconAdjustmentsHorizontal,
    IconArrowsSort,
    IconSearch,
    IconSortAscending,
    IconSortDescending
} from '@tabler/icons-react';

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
    /** Modal to show if a row is clicked */
    viewModal?: (value: T, onClose: () => void) => ReactNode;
}

export default function OverviewPage<T>({
    identifier,
    fetchData,
    columns,
    manipulationDialog,
    deletionDialogTitle,
    onDelete,
    idKey,
    viewModal = () => null
}: OverviewPageProps<T>) {
    const {t} = useTranslation();
    const {activeUniverse} = useUniverseContext();
    const {userPermissions} = useUserContext();
    const [data, refresh, loading] = fetchData;

    const [lastClicked, setLastClicked] = useState<T>(null);
    const [sorting, setSorting] = useState<SortingState>([]);
    const [globalFilter, setGlobalFilter] = useState('');
    const [rowSelection, setRowSelection] = useState({});
    const [columnVisibility, setColumnVisibility] = useLocalStorage<Record<string, boolean>>({
        key: identifier + '-visibility',
        defaultValue: columns.reduce<Record<string, boolean>>((rec, col) => {
            const key: string = col.id;
            rec[key] = !col.defaultHidden;
            return rec;
        }, {})
    });

    const table = useReactTable({
        data,
        columns: [
            {
                id: 'select',
                header: ({table: t}) => (
                    <Checkbox
                        checked={t.getIsAllRowsSelected()}
                        indeterminate={t.getIsSomeRowsSelected()}
                        onChange={t.getToggleAllRowsSelectedHandler()}
                    />
                ),
                cell: ({row}) => (
                    <Checkbox
                        checked={row.getIsSelected()}
                        disabled={!row.getCanSelect()}
                        onChange={row.getToggleSelectedHandler()}
                        onClick={e => e.stopPropagation()}
                    />
                ),
            },
            ...columns
        ],
        state: {
            sorting,
            globalFilter,
            rowSelection,
            columnVisibility,
        },
        onSortingChange: setSorting,
        onGlobalFilterChange: setGlobalFilter,
        onRowSelectionChange: setRowSelection,
        onColumnVisibilityChange: setColumnVisibility,
        getCoreRowModel: getCoreRowModel(),
        getSortedRowModel: getSortedRowModel(),
        getFilteredRowModel: getFilteredRowModel(),
        globalFilterFn: (row, _, filterValue, addMeta) => {
            const searchableColumns: Column<T>[] = table.getAllColumns();

            return searchableColumns.some(column => {
                const columnFilterFn = column.columnDef.filterFn;
                const value = row.getValue(column.id);

                if (typeof columnFilterFn === 'function') {
                    return columnFilterFn(row, column.id, filterValue, addMeta);
                }

                // Fallback for columns without a custom filterFn
                return String(value)
                    .toLowerCase()
                    .includes(String(filterValue).toLowerCase());
            });
        }
    });

    return <Stack>
        <Paper p="md" withBorder radius="md">
            <Stack gap="md">
                {/* Toolbar: Search & Column Visibility */}
                <Group justify="space-between">
                    <TextInput
                        placeholder="Search all columns..."
                        leftSection={<IconSearch size={16}/>}
                        value={globalFilter ?? ''}
                        onChange={(e) => setGlobalFilter(e.target.value)}
                        style={{flex: 1, maxWidth: 300}}
                    />

                    <Menu closeOnItemClick={false} shadow="md" width={200}>
                        <Menu.Target>
                            <Button variant="outline" leftSection={<IconAdjustmentsHorizontal size={16}/>}>
                                Columns
                            </Button>
                        </Menu.Target>
                        <Menu.Dropdown>
                            <Menu.Label>Toggle Columns</Menu.Label>
                            {table.getAllLeafColumns().map((column) => (
                                column.id !== 'select' ? (
                                    <Menu.Item key={column.id}>
                                        <Checkbox
                                            label={column.columnDef.header as string}
                                            checked={column.getIsVisible()}
                                            onChange={column.getToggleVisibilityHandler()}
                                        />
                                    </Menu.Item>
                                ) : null
                            ))}
                        </Menu.Dropdown>
                    </Menu>
                </Group>

                {/* The Table */}
                <Table.ScrollContainer minWidth={500}>
                    <Table variant="simple" verticalSpacing="sm" withTableBorder withColumnBorders>
                        <Table.Thead>
                            {table.getHeaderGroups().map((headerGroup) => (
                                <Table.Tr key={headerGroup.id}>
                                    {headerGroup.headers.map((header) => (
                                        <Table.Th
                                            key={header.id}
                                            style={{
                                                width: header.column.id === 'select' ? rem(40) : 'auto',
                                                cursor: header.column.getCanSort() ? 'pointer' : 'default',
                                                userSelect: 'none'
                                            }}
                                            onClick={header.column.getToggleSortingHandler()}
                                        >
                                            <Group gap="xs" wrap="nowrap">
                                                <Text fw={700} size="sm" component={'span'}>
                                                    {flexRender(header.column.columnDef.header, header.getContext())}
                                                </Text>
                                                {header.column.getCanSort() && (
                                                    <Box style={{opacity: header.column.getIsSorted() ? 1 : 0.3}}>
                                                        {
                                                            {
                                                                asc: <IconSortAscending size={14}/>,
                                                                desc: <IconSortDescending size={14}/>,
                                                            }[header.column.getIsSorted() as string] ??
                                                            <IconArrowsSort size={14}/>
                                                        }
                                                    </Box>
                                                )}
                                            </Group>
                                        </Table.Th>
                                    ))}
                                </Table.Tr>
                            ))}
                        </Table.Thead>

                        <Table.Tbody>
                            {table.getRowModel().rows.length > 0 ? (
                                table.getRowModel().rows.map((row) => (
                                    <Table.Tr
                                        key={row.id}
                                        data-testid={row.original[idKey]}
                                        bg={row.getIsSelected() ? 'var(--mantine-color-blue-light)' : undefined}
                                        onClick={() => setLastClicked(row.original)}
                                    >
                                        {row.getVisibleCells().map((cell) => (
                                            <Table.Td
                                                key={cell.id}
                                                style={{
                                                    width: cell.column.id === 'select' ? rem(40) : 'auto'
                                                }}
                                            >
                                                {flexRender(cell.column.columnDef.cell, cell.getContext())}
                                            </Table.Td>
                                        ))}
                                    </Table.Tr>
                                ))
                            ) : (
                                <Table.Tr>
                                    <Table.Td colSpan={columns.length}>
                                        <Text py="xl" c="dimmed">No results found.</Text>
                                    </Table.Td>
                                </Table.Tr>
                            )}
                        </Table.Tbody>
                    </Table>
                </Table.ScrollContainer>
            </Stack>
        </Paper>
        {userPermissions.canWriteActiveUniverse && <Group justify="flex-end">
            {manipulationDialog(false, refresh, false, () => undefined)}
            {manipulationDialog(true, refresh, table.getSelectedRowModel().flatRows.length !== 1, () => table.getSelectedRowModel().flatRows[0].original)}
            <ConfirmationDialog
                title={deletionDialogTitle}
                onConfirmation={() => onDelete(activeUniverse?.id, table.getSelectedRowModel().flatRows.map(row => row.original))
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
        {viewModal(lastClicked, () => setLastClicked(null))}
    </Stack>;
}