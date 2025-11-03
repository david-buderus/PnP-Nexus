import {useNode} from "@craftjs/core";
import {NumberInput, Stack, Table, Textarea, TextInput} from "@mantine/core";
import React from "react";
import {EMPTY_TABLE_ROW_HEIGHT, getPartStyle, TABLE_STYLE} from "../Constants";
import {useTranslation} from "react-i18next";

/** Part to show text */
export const TextFieldPart = ({text, title, numberOfRows}: {
    text: string;
    title: string;
    numberOfRows: number;
}) => {
    const {connectors: {connect, drag}, selected} = useNode((state => ({
        selected: state.events.selected
    })));

    return <Table
        withTableBorder
        striped
        ref={ref => connect(drag(ref))}
        style={getPartStyle(selected)}
    >
        <Table.Tbody>
            <Table.Tr h={EMPTY_TABLE_ROW_HEIGHT + 3}>
                <Table.Th style={TABLE_STYLE}>{title}</Table.Th>
            </Table.Tr>
            <Table.Tr h={EMPTY_TABLE_ROW_HEIGHT * numberOfRows}>
                <Table.Td style={{whiteSpace: 'pre-line', ...TABLE_STYLE}}>
                    {text}
                </Table.Td>
            </Table.Tr>
        </Table.Tbody>
    </Table>;
};

const TextFieldPartSettings = () => {
    const {t} = useTranslation();
    const {actions: {setProp}, title, text, numberOfRows} = useNode(node => ({
        title: node.data.props.title,
        text: node.data.props.text,
        numberOfRows: node.data.props.numberOfRows
    }));

    return <Stack>
        <TextInput
            label={t("sheetEditor:title")}
            value={title}
            onChange={e => {
                setProp(props => props.title = e.currentTarget.value);
            }}
        />
        <Textarea
            label={t("sheetEditor:content")}
            value={text}
            onChange={e => {
                setProp(props => {
                    props.text = e.currentTarget.value;
                });
            }}
            rows={5}
        />
        <NumberInput
            value={numberOfRows}
            onChange={e => {
                setProp(props => {
                    props.numberOfRows = Number(e);
                });
            }}
            min={1}
        />
    </Stack>;
};

TextFieldPart.craft = {
    name: "sheetEditor:textField",
    related: {
        settings: TextFieldPartSettings
    }
};