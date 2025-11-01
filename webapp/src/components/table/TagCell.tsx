import {Group, Pill} from '@mantine/core';
import {MRT_Cell} from 'mantine-react-table';
import {Row} from '@tanstack/table-core/build/lib/types';

/** Renders a set of tags as a cell */
export default function TagCell({cell}: { cell: MRT_Cell<any, string[]>; }) {

    return <Group gap="xs">
        {cell.getValue()?.map(tag => <Pill key={tag}>{tag}</Pill>)}
    </Group>;
}

/** Filter function for TagCell */
export function filterTagCell(row: Row<any>, id: string, filterValue: any) {
    return row.getValue<string[]>(id).some(tag => tag.includes(filterValue));
}