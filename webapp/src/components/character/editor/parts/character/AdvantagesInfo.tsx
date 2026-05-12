import {List, Switch, Table, Text} from '@mantine/core';
import React, {useContext, useMemo} from 'react';
import {TABLE_ROW_HEIGHT, TABLE_STYLE} from '../Constants';
import {useTranslation} from 'react-i18next';
import {PnPCharacterContext} from '../../../PnPCharacterContext';
import {PageElementSettings} from '../PageElementSettings';

/** Part to show text */
export function AdvantagesInfo({showsAdvantages, setShowsAdvantages}: {
    showsAdvantages: boolean;
    setShowsAdvantages: (b: boolean) => void;
}) {
    const {t} = useTranslation();
    const {characterForm} = useContext(PnPCharacterContext);
    const character = characterForm.getValues();

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

    return <>
        <Table
            withTableBorder
            striped
            style={{height: '100%', tableLayout: 'fixed'}}
        >
            <Table.Tbody>
                <Table.Tr h={TABLE_ROW_HEIGHT + 3}>
                    <Table.Th style={TABLE_STYLE}>
                        {showsAdvantages ? t('advantages') : t('disadvantages')}
                    </Table.Th>
                </Table.Tr>
                <Table.Tr>
                    <Table.Td
                        style={{whiteSpace: 'pre-line', textAlign: 'left', verticalAlign: 'top', ...TABLE_STYLE}}>
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
        </Table>
        <PageElementSettings>
            <Switch
                label={showsAdvantages ? t('advantages') : t('disadvantages')}
                checked={showsAdvantages}
                onChange={e => setShowsAdvantages(e.target.checked)}
            />
        </PageElementSettings>
    </>;
}
