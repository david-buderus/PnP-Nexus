import {Row} from '@tanstack/table-core/build/lib/types';
import {ReactNode} from 'react';
import {CellContext} from '@tanstack/table-core';

/** A simple interface for an object with a name */
export interface NamedObject {
    /** Named of the object */
    name: string;
}

/** Renders an object with a name as a cell */
export function NamedCell<T>(cell: CellContext<T, NamedObject>): ReactNode {
    return cell.getValue()?.name ?? '';
}

/** Filter function for NamedCells */
export function filterNamedCell(row: Row<any>, id: string, filterValue: any) {
    return row.getValue<NamedObject>(id)?.name?.toLowerCase()?.includes(String(filterValue).toLowerCase());
}

/** Renders multi objects with a name as a cell */
export function MultiNamedCell<T>(cell: CellContext<T, NamedObject[]>): ReactNode {
    return cell.getValue()?.map(o => o?.name ?? '-').join(', ');
}

/** Filter function for MultiNamedCell */
export function filterMultiNamedCell(row: Row<any>, id: string, filterValue: any) {
    return row.getValue<NamedObject[]>(id)?.some(o => o?.name?.toLowerCase()?.includes(String(filterValue).toLowerCase()));
}