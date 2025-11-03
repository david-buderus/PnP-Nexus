import {useNode} from "@craftjs/core";
import {ActionIcon, Card, Group, Stack, Table} from "@mantine/core";
import {getPartStyle, TABLE_STYLE} from "../Constants";
import React, {useContext} from "react";
import {PnPCharacterContext} from "../../PnPCharacterContext";
import {PrimaryAttribute} from "../../../../../api";
import {FaChevronDown, FaChevronUp} from "react-icons/fa6";

/** Shows level and co of the character */
export const PrimaryAttributeRow = ({
    attributes
}: {
    attributes: PrimaryAttribute[]
}) => {
    const {character} = useContext(PnPCharacterContext);
    const {connectors: {connect, drag}, selected} = useNode((state => ({
        selected: state.events.selected
    })));

    return <Table
        withTableBorder
        withColumnBorders
        ref={ref => connect(drag(ref))}
        style={getPartStyle(selected)}
    >
        <Table.Tbody>
            <Table.Tr>
                {attributes.map((attribute) => (
                    <Table.Th key={attribute.id} style={TABLE_STYLE}>{attribute.shortName}</Table.Th>
                ))}
            </Table.Tr>
            <Table.Tr>
                {attributes.map((attribute) => (
                    <Table.Th key={attribute.id}
                              style={TABLE_STYLE}>{character?.stats.primaryStats[attribute.id]?.rawValue ?? 0}</Table.Th>
                ))}
            </Table.Tr>
        </Table.Tbody>
    </Table>;
};

const PrimaryAttributeRowSettings = () => {
    const {actions: {setProp}, attributesOrder} = useNode(node => ({
        attributesOrder: node.data.props.attributes
    }));

    function moveUp(index: number) {
        const copy = [...attributesOrder];
        const item = copy.splice(index, 1)[0];
        copy.splice(index - 1, 0, item);
        setProp(props => {
            props.attributes = copy;
        });
    }

    function moveDown(index: number) {
        const copy = [...attributesOrder];
        const item = copy.splice(index, 1)[0];
        copy.splice(index + 1, 0, item);
        setProp(props => {
            props.attributes = copy;
        });
    }

    return <Stack gap={1}>
        {attributesOrder.map((attribute: PrimaryAttribute, index: number) => (
            <Card key={attribute.id} shadow="sm">
                <Group wrap="nowrap" justify="space-between">
                    {attribute.name}
                    <Group wrap="nowrap" gap={0}>
                        <ActionIcon
                            variant="outline"
                            disabled={index === 0}
                            onClick={() => moveUp(index)}
                        >
                            <FaChevronUp/>
                        </ActionIcon>
                        <ActionIcon
                            variant="outline"
                            disabled={index === attributesOrder.length - 1}
                            onClick={() => moveDown(index)}
                        >
                            <FaChevronDown/>
                        </ActionIcon>
                    </Group>
                </Group>
            </Card>
        ))}
    </Stack>;
};

PrimaryAttributeRow.craft = {
    name: "sheetEditor:primaryAttributeRow",
    related: {
        settings: PrimaryAttributeRowSettings
    }
};