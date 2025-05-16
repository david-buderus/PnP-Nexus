import { Row } from "@tanstack/table-core/build/lib/types";
import { MRT_Cell } from "mantine-react-table";

/** A simple interace for an object with a name */
export interface NamedObject {
    name: string;
}

/** Renders an object with a name as a cell */
export function NamedCell({ cell }: { cell: MRT_Cell<any, NamedObject>; }) {
    return cell.getValue()?.name ?? "";
}

/** Filter function for NamedCells */
export function filterNamedCell(row: Row<any>, id: string, filterValue: any) {
    return row.getValue<NamedObject>(id)?.name?.includes(filterValue);
}

/** Renders multi objects with a name as a cell */
export function MultiNamedCell({ cell }: { cell: MRT_Cell<any, NamedObject[]>; }) {
    return cell.getValue()?.map(o => o?.name ?? "-").join(", ");
}

/** Filter function for MultiNamedCell */
export function filterMultiNamedCell(row: Row<any>, id: string, filterValue: any) {
    return row.getValue<NamedObject[]>(id)?.some(o => o?.name?.includes(filterValue));
}