import React, {ReactNode} from "react";
import {useNode} from "@craftjs/core";
import {Group, Select, Stack} from "@mantine/core";
import {getPartStyle} from "../Constants";

/** A part which contains {@link Group} */
export const GroupPart = ({children, ...props}: {
    children?: ReactNode;
    justify?: "flex-start" | "flex-end" | "center" | "space-between";
    align?: "flex-start" | "flex-end" | "center";
}) => {
    const {
        connectors: {connect, drag},
        selected,
    } = useNode(node => ({
        selected: node.events.selected,
    }));

    const isEmpty = React.Children.count(children) === 0;

    return (
        <div
            ref={(ref) => ref && connect(drag(ref))}
            style={getPartStyle(selected, isEmpty)}
        >
            <Group wrap="nowrap" {...props}>{children}</Group>
        </div>
    );
};

const GroupSettings = () => {
    const {actions: {setProp}, justify, align} = useNode(node => ({
        justify: node.data.props.justify,
        align: node.data.props.align
    }));

    return <Stack>
        <Select
            label="Justify"
            value={justify ?? 'flex-start'}
            onChange={e => setProp(props => props.justify = e)}
            data={["flex-start", "flex-end", "center", "space-between"]}
        />
        <Select
            label="Align"
            value={align ?? 'center'}
            onChange={e => setProp(props => props.align = e)}
            data={["flex-start", "flex-end", "center"]}
        />
    </Stack>;
};

GroupPart.craft = {
    name: "sheetEditor:horizontal",
    related: {
        settings: GroupSettings
    }
};