import {Row} from "@tanstack/table-core/build/lib/types";
import {MRT_Cell} from "mantine-react-table";
import {ReactNode} from "react";

/** A simple interface for an object with a name */
export interface NamedObject {
    /** Named of the object */
    name: string;
}

/** Renders an object with a name as a cell */
export function NamedCell({cell}: { cell: MRT_Cell<any, NamedObject>; }): ReactNode {
    return cell.getValue()?.name ?? "";
}

/** Filter function for NamedCells */
export function filterNamedCell(row: Row<any>, id: string, filterValue: any) {
    return row.getValue<NamedObject>(id)?.name?.includes(filterValue);
}

/** Renders multi objects with a name as a cell */
export function MultiNamedCell({cell}: { cell: MRT_Cell<any, NamedObject[]>; }): ReactNode {
    return cell.getValue()?.map(o => o?.name ?? "-").join(", ");
}

/** Filter function for MultiNamedCell */
export function filterMultiNamedCell(row: Row<any>, id: string, filterValue: any) {
    return row.getValue<NamedObject[]>(id)?.some(o => o?.name?.includes(filterValue));
}