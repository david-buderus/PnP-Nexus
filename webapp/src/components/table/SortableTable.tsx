import React, {useState} from 'react';
import {Box, Button, Checkbox, Group, Menu, Paper, rem, Stack, Table, Text, TextInput} from '@mantine/core';
import {
    ColumnDef,
    flexRender,
    getCoreRowModel,
    getFilteredRowModel,
    getSortedRowModel,
    SortingState,
    useReactTable
} from '@tanstack/react-table';
import {
    IconAdjustmentsHorizontal,
    IconArrowsSort,
    IconSearch,
    IconSortAscending,
    IconSortDescending
} from '@tabler/icons-react';
import {useLocalStorage} from '@mantine/hooks';

export type CustomTableProps<T extends object> = {
    id: string;
    data: T[];
    columns: ExtendedColumnDef<T, any>[];
}

/**
 * An extended format of the column definition.
 * Including a hidden parameter.
 */
export type ExtendedColumnDef<T, S> = {
    /** If the column should be hidden by default */
    defaultHidden?: boolean;
} & ColumnDef<T, S>;

export function SortableTable<T extends object>({id, data, columns}: CustomTableProps<T>) {
    const [sorting, setSorting] = useState<SortingState>([]);
    const [globalFilter, setGlobalFilter] = useState('');
    const [columnVisibility, setColumnVisibility] = useLocalStorage<Record<string, boolean>>({
        key: id + '-visibility',
        defaultValue: columns.reduce<Record<string, boolean>>((rec, col) => {
            const key: string = col.id;
            rec[key] = !col.defaultHidden;
            return rec;
        }, {})
    });

    const table = useReactTable({
        data,
        columns,
        state: {
            sorting,
            globalFilter,
            columnVisibility,
        },
        onSortingChange: setSorting,
        onGlobalFilterChange: setGlobalFilter,
        onColumnVisibilityChange: setColumnVisibility,
        getCoreRowModel: getCoreRowModel(),
        getSortedRowModel: getSortedRowModel(),
        getFilteredRowModel: getFilteredRowModel(),
    });

    return (
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
                                                <Text fw={700} size="sm">
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
                                        bg={row.getIsSelected() ? 'var(--mantine-color-blue-light)' : undefined}
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
    );
}