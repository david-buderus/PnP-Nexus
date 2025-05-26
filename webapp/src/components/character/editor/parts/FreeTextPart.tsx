import {useNode} from "@craftjs/core";
import {Stack, Text, TextInput} from "@mantine/core";
import React from "react";

export const FreeTextPart = ({text, fontSize}: {
    text: string;
    fontSize?: string;
}) => {
    const {connectors: {connect, drag}, hasSelectedNode} = useNode(((state) => ({
        hasSelectedNode: state.events.selected
    })));

    return <Text
        ref={ref => connect(drag(ref))}
        style={{
            border: hasSelectedNode ? "1px solid gray" : undefined,
        }}
        size={fontSize}
    >
        {text}
    </Text>;
};

const FreeTextSettings = () => {
    const {actions: {setProp}, fontSize, text} = useNode((node) => ({
        fontSize: node.data.props.fontSize,
        text: node.data.props.text
    }));

    return <Stack>
        <TextInput
            value={text}
            onChange={(e) => setProp(props => props.text = e.currentTarget.value)}
        />
    </Stack>;
};

FreeTextPart.craft = {
    related: {
        settings: FreeTextSettings
    }
};