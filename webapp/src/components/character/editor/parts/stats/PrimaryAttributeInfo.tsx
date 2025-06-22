import {useNode} from "@craftjs/core";
import {ActionIcon, Card, Group, Stack, Table} from "@mantine/core";
import {getPartStyle, TABLE_STYLE} from "../Constants";
import React, {useContext} from "react";
import {useTranslation} from "react-i18next";
import {PnPCharacterContext} from "../../PnPCharacterContext";
import {useUniverseContext} from "../../../../PageBase";
import {PrimaryAttribute} from "../../../../../api";
import {FaChevronDown, FaChevronUp} from "react-icons/fa6";

/** Shows level and co of the character */
export const PrimaryAttributeInfo = ({
    attributes
}: {
    attributes: PrimaryAttribute[]
}) => {
    const {t} = useTranslation();
    const {character} = useContext(PnPCharacterContext);
    const {characterSettings} = useUniverseContext();
    const {connectors: {connect, drag}, selected} = useNode((state => ({
        selected: state.events.selected
    })));

    return <Table
        variant="vertical"
        layout="fixed"
        withTableBorder
        ref={ref => connect(drag(ref))}
        style={getPartStyle(selected)}
    >
        <Table.Tbody>
            <Table.Tr>
                <Table.Th colSpan={2} style={TABLE_STYLE}>{t("primary-attributes")}</Table.Th>
                <Table.Th
                    style={TABLE_STYLE}>{`Min: ${characterSettings.minPrimaryAttributeValue} Max: ${characterSettings.maxPrimaryAttributeValue}`}</Table.Th>
            </Table.Tr>

            {attributes.map((attribute) => (
                <Table.Tr key={attribute.id}>
                    <Table.Th style={TABLE_STYLE}>{attribute.name}</Table.Th>
                    <Table.Th style={TABLE_STYLE}>{attribute.shortName}</Table.Th>
                    <Table.Td style={TABLE_STYLE}>
                        {character?.stats.primaryStats[attribute.id]?.rawValue ?? 0}
                    </Table.Td>
                </Table.Tr>
            ))}
        </Table.Tbody>
    </Table>;
};

const PrimaryAttributeSettings = () => {
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

PrimaryAttributeInfo.craft = {
    name: "sheetEditor:primaryAttributeInfo",
    related: {
        settings: PrimaryAttributeSettings
    }
};