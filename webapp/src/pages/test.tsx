import {Badge, Button, Group, Paper, Stack, Switch, Text, TextInput, Title} from "@mantine/core";
import {Editor, Element, Frame, useEditor, useNode} from "@craftjs/core";
import React, {ReactNode} from "react";

const TextPart = ({text, fontSize}: {
    text: string;
    fontSize?: string;
}) => {
    const {connectors: {connect, drag}} = useNode();

    return <Text
        ref={ref => connect(drag(ref))}
        size={fontSize}
    >
        {text}
    </Text>;
};

const TextSettings = () => {
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

TextPart.craft = {
    related: {
        settings: TextSettings
    }
};

const StackPart = ({children}: {
    children?: ReactNode;
}) => {
    const {connectors: {connect, drag}, hasSelectedNode} = useNode(((state) => ({
        hasSelectedNode: state.events.selected
    })));

    return (
        <Stack ref={ref => connect(drag(ref))} style={{
            border: hasSelectedNode ? "1px solid gray" : undefined,
        }}>
            {children}
        </Stack>
    );
};

const GroupPart = ({children}: {
    children?: ReactNode;
}) => {
    const {connectors: {connect, drag}} = useNode();
    return (
        <Group ref={ref => connect(drag(ref))}>
            {children}
        </Group>
    );
};

const Toolbox = () => {
    const {connectors, query} = useEditor();

    return <Stack>
        <Title order={3}>
            Drag top add
        </Title>
        <Button ref={ref => connectors.create(ref, <TextPart text="Test"/>)}>
            Text
        </Button>
        <Button ref={ref => connectors.create(ref, <Element is={StackPart} canvas/>)}>
            Stack
        </Button>
        <Button ref={ref => connectors.create(ref, <Element is={GroupPart} canvas/>)}>
            Group
        </Button>
    </Stack>;
};

const SettingsPanel = () => {
    const {actions, selected} = useEditor((state, query) => {
        const [currentNodeId] = Array.from(state.events.selected);
        let selected;

        if (currentNodeId) {
            selected = {
                id: currentNodeId,
                name: state.nodes[currentNodeId].data.name,
                settings: state.nodes[currentNodeId].related && state.nodes[currentNodeId].related.settings,
                isDeletable: query.node(currentNodeId).isDeletable()
            };
        }

        return {
            selected
        };
    });

    return <Stack>
        <Group wrap="nowrap">
            <Text>
                Selected
            </Text>
            <Badge>
                {selected?.name}
            </Badge>
        </Group>
        {
            selected?.settings && React.createElement(selected.settings)
        }
        <Button disabled={!selected?.isDeletable} onClick={() => actions.delete(selected.id)}>
            Delete
        </Button>
    </Stack>;
};

const TopBar = () => {
    return <Group wrap="nowrap">
        <Switch label="Enable"/>
        <Button>
            Serialize
        </Button>
    </Group>;
};

export function Test() {

    return <Stack>
        <Title>Example</Title>
        <Editor resolver={{TextPart, StackPart, GroupPart}} enabled={true}>
            <TopBar/>
            <Group align="space-around" grow>
                <Paper shadow="md">
                    <Frame>
                        <Element is={StackPart} canvas>
                            <TextPart text="Test"/>
                            <Element is={GroupPart} canvas>
                                <TextPart text="Test"/>
                            </Element>
                        </Element>
                    </Frame>
                </Paper>
                <Paper>
                    <Stack maw={200} align="flex-end">
                        <Toolbox/>
                        <SettingsPanel/>
                    </Stack>
                </Paper>
            </Group>
        </Editor>
    </Stack>;
}