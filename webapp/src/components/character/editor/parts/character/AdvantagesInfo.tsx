import {useNode} from '@craftjs/core';
import {List, NumberInput, Stack, Switch, Table, Text} from '@mantine/core';
import React, {useContext, useMemo} from 'react';
import {getPartStyle, TABLE_ROW_HEIGHT, TABLE_STYLE} from '../Constants';
import {useTranslation} from 'react-i18next';
import {PnPCharacterContext} from '../../../PnPCharacterContext';

/** Part to show text */
export const AdvantagesInfo = ({showsAdvantages, numberOfRows}: {
    showsAdvantages: boolean;
    numberOfRows: number;
}) => {
    const {t} = useTranslation();
    const {characterForm} = useContext(PnPCharacterContext);
    const character = characterForm.getValues();
    const {connectors: {connect, drag}, selected} = useNode((state => ({
        selected: state.events.selected
    })));

    const entries = useMemo(() => {
        if (!character) {
            return [];
        }
        if (showsAdvantages) {
            return character.advantageTraits;
        } else {
            return character.disadvantageTraits;
        }
    }, [character, showsAdvantages]);

    return <Table
        withTableBorder
        striped
        ref={ref => connect(drag(ref))}
        style={getPartStyle(selected)}
    >
        <Table.Tbody>
            <Table.Tr h={TABLE_ROW_HEIGHT + 3}>
                <Table.Th style={TABLE_STYLE}>
                    {showsAdvantages ? t('advantages') : t('disadvantages')}
                </Table.Th>
            </Table.Tr>
            <Table.Tr h={TABLE_ROW_HEIGHT * numberOfRows}>
                <Table.Td style={{whiteSpace: 'pre-line', textAlign: 'left', verticalAlign: 'top', ...TABLE_STYLE}}>
                    <List size="sm">
                        {entries.map((entry, index) => (
                            <List.Item key={index}>
                                <Text size="10px">
                                    {entry.description}
                                </Text>
                            </List.Item>
                        ))}
                    </List>
                </Table.Td>
            </Table.Tr>
        </Table.Tbody>
    </Table>;
};

const AdvantagesInfoSettings = () => {
    const {t} = useTranslation();
    const {actions: {setProp}, showsAdvantages, numberOfRows} = useNode(node => ({
        showsAdvantages: node.data.props.showsAdvantages as boolean,
        numberOfRows: node.data.props.numberOfRows
    }));

    return <Stack>
        <Switch
            label={showsAdvantages ? t('advantages') : t('disadvantages')}
            checked={showsAdvantages}
            onChange={e => {
                setProp(props => {
                    props.showsAdvantages = e.target.checked;
                });
            }}
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

AdvantagesInfo.craft = {
    name: 'advantages',
    related: {
        settings: AdvantagesInfoSettings
    }
};