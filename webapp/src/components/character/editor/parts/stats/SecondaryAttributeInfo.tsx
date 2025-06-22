import {useNode} from "@craftjs/core";
import {ActionIcon, Card, Group, Stack, Table} from "@mantine/core";
import {getPartStyle, TABLE_STYLE} from "../Constants";
import React, {useContext} from "react";
import {useTranslation} from "react-i18next";
import {PnPCharacterContext} from "../../PnPCharacterContext";
import {SecondaryAttribute} from "../../../../../api";
import {FaChevronDown, FaChevronUp} from "react-icons/fa6";

/** Shows level and co of the character */
export const SecondaryAttributeInfo = ({
    attributes
}: {
    attributes: SecondaryAttribute[];
}) => {
    const {t} = useTranslation();
    const {character} = useContext(PnPCharacterContext);
    const {connectors: {connect, drag}, selected} = useNode((state => ({
        selected: state.events.selected
    })));

    return <Table
        variant="vertical"
        withTableBorder
        ref={ref => connect(drag(ref))}
        style={getPartStyle(selected)}
    >
        <Table.Tbody>
            <Table.Tr>
                <Table.Th style={TABLE_STYLE} colSpan={4}>{t("secondary-attributes")}</Table.Th>
            </Table.Tr>

            {attributes.map((attribute) => (
                <Table.Tr key={attribute.id}>
                    <Table.Th style={{width: "45%", ...TABLE_STYLE}}>{attribute.name}</Table.Th>
                    <Table.Th style={{width: "15%", ...TABLE_STYLE}}>{attribute.shortName}</Table.Th>
                    <Table.Td style={(theme) => ({
                        width: "20%",
                        borderRight: `1px solid ${theme.colors.gray[3]}`,
                        ...TABLE_STYLE
                    })}></Table.Td>
                    <Table.Td style={{width: "20%", ...TABLE_STYLE}}>
                        {character?.stats.secondaryStats[attribute.id]?.rawValue ?? 0}
                    </Table.Td>
                </Table.Tr>
            ))}
        </Table.Tbody>
    </Table>;
};

const SecondaryAttributeSettings = () => {
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
        {attributesOrder.map((attribute: SecondaryAttribute, index: number) => (
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

SecondaryAttributeInfo.craft = {
    name: "sheetEditor:secondaryAttributeInfo",
    related: {
        settings: SecondaryAttributeSettings
    }
};