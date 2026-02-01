import {Group, Pill} from '@mantine/core';
import {Row} from '@tanstack/table-core/build/lib/types';
import {CellContext} from '@tanstack/table-core';

/** Renders a set of tags as a cell */
export default function TagCell<T>(cell: CellContext<T, string[]>) {

    return <Group gap="xs">
        {cell.getValue()?.map(tag => <Pill key={tag}>{tag}</Pill>)}
    </Group>;
}

/** Filter function for TagCell */
export function filterTagCell<T>(row: Row<T>, id: string, filterValue: any) {
    return row.getValue<string[]>(id).some(tag => tag.includes(filterValue));
}