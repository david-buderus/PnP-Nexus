import {useNode} from '@craftjs/core';
import {NumberInput, Stack, Table, TextInput, Tooltip} from '@mantine/core';
import React, {useContext} from 'react';
import {getPartStyle, TABLE_ROW_HEIGHT, TABLE_STYLE} from '../Constants';
import {useTranslation} from 'react-i18next';
import {PnPCharacterContext} from '../../PnPCharacterContext';

/** Part to show text from a custom field */
export const TextFieldPart = ({title, customId, numberOfRows}: {
    title: string;
    customId: string;
    numberOfRows: number;
}) => {
    const {connectors: {connect, drag}, selected} = useNode((state => ({
        selected: state.events.selected
    })));
    const {character} = useContext(PnPCharacterContext);

    return <Table
        withTableBorder
        striped
        ref={ref => connect(drag(ref))}
        style={getPartStyle(selected)}
    >
        <Table.Tbody>
            <Table.Tr h={TABLE_ROW_HEIGHT + 3}>
                <Table.Th style={TABLE_STYLE}>{title}</Table.Th>
            </Table.Tr>
            <Table.Tr h={TABLE_ROW_HEIGHT * numberOfRows}>
                <Table.Td style={{whiteSpace: 'pre-line', ...TABLE_STYLE}}>
                    {character?.customFields?.[customId] ?? ''}
                </Table.Td>
            </Table.Tr>
        </Table.Tbody>
    </Table>;
};

const TextFieldPartSettings = () => {
    const {t} = useTranslation();
    const {actions: {setProp}, title, customId, numberOfRows} = useNode(node => ({
        title: node.data.props.title,
        customId: node.data.props.customId,
        numberOfRows: node.data.props.numberOfRows
    }));

    return <Stack>
        <TextInput
            label={t('sheetEditor:title')}
            value={title}
            onChange={e => {
                setProp(props => props.title = e.currentTarget.value);
            }}
        />
        <Tooltip label={t('sheetEditor:customFieldIdTooltip')}>
            <TextInput
                label={t('sheetEditor:customFieldId')}
                value={customId}
                onChange={e => {
                    setProp(props => {
                        props.customId = e.currentTarget.value;
                    });
                }}
            />
        </Tooltip>
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
    name: 'sheetEditor:textField',
    related: {
        settings: TextFieldPartSettings
    }
};