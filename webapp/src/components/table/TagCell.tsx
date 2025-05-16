import { Group, Pill } from "@mantine/core";
import { MRT_Cell } from "mantine-react-table";

/** Renders a set of tags as a cell */
export default function TagCell({ cell }: { cell: MRT_Cell<any, string[]>; }) {

    return <Group gap="xs">
        {cell.getValue().map(tag => <Pill key={tag}>{tag}</Pill>)}
    </Group>;
}