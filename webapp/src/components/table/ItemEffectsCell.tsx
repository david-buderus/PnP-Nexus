import {Row} from '@tanstack/table-core/build/lib/types';
import {ReactNode} from 'react';
import {CellContext} from '@tanstack/table-core';
import {SomeItemEffect} from '../Constants';
import {List} from '@mantine/core';

/** Renders item effects */
export function ItemEffectsCell<T>(cell: CellContext<T, SomeItemEffect[]>): ReactNode {
    const effects = cell.getValue<SomeItemEffect[]>();
    if (effects.length < 2) {
        return effects[0]?.description ?? '';
    } else {
        return <List>
            {effects.map((effect, index) => <List.Item key={index}>
                {effect.description}
            </List.Item>)}
        </List>;
    }
}

/** Filter function for NamedCells */
export function filterItemEffectsCell(row: Row<any>, id: string, filterValue: any) {
    return row.getValue<SomeItemEffect[]>(id).some(effect => effect.description.toLowerCase().includes(String(filterValue).toLowerCase()));
}
