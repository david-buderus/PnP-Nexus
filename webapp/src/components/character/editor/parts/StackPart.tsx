import React, {ReactNode} from "react";
import {useNode} from "@craftjs/core";
import {Stack} from "@mantine/core";
import {highlightStyle} from "./Constants";

export const StackPart = ({children, ...props}: {
    children?: ReactNode;
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
            <Stack {...props}>{children}</Stack>
        </div>
    );
};