import React, {ReactNode} from "react";
import {useNode} from "@craftjs/core";
import {Group} from "@mantine/core";
import {highlightStyle} from "./Constants";

export const GroupPart = ({children, ...props}: {
    children?: ReactNode;
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
            style={selected || isEmpty ? highlightStyle : undefined}
        >
            <Group {...props}>{children}</Group>
        </div>
    );
};