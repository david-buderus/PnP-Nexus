import React, {ReactNode} from "react";
import {useNode} from "@craftjs/core";
import {SimpleGrid} from "@mantine/core";
import {getPartStyle} from "../Constants";

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

    const isEmpty = React.Children.count(children) === 0;

    return (
        <div
            ref={(ref) => ref && connect(drag(ref))}
            style={getPartStyle(selected, isEmpty)}
        >
            <SimpleGrid {...props}>{children}</SimpleGrid>
        </div>
    );
};