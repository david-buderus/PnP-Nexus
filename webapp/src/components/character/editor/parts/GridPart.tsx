import React, {ReactNode} from "react";
import {useNode} from "@craftjs/core";
import {Grid} from "@mantine/core";
import {highlightStyle} from "./Constants";

export const GridPart = ({children, ...props}: {
    children?: ReactNode;
    columns: number;
    rows: number;
}) => {
    const {
        connectors: {connect, drag},
        selected,
    } = useNode((node) => ({
        selected: node.events.selected,
    }));

    return (
        <div
            ref={(ref) => ref && connect(drag(ref))}
            style={selected ? highlightStyle : undefined}
        >
            <Grid {...props}>{children}</Grid>
        </div>
    );
};