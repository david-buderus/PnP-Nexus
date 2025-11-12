import {useNode} from '@craftjs/core';
import {Table} from '@mantine/core';
import {getPartStyle, TABLE_ROW_HEIGHT, TABLE_STYLE} from '../Constants';
import React, {useContext} from 'react';
import {useTranslation} from 'react-i18next';
import {PnPCharacterContext} from '../../PnPCharacterContext';

/** Shows name and co of the character */
export const CharacterInfo = () => {
    const {t} = useTranslation();
    const {character} = useContext(PnPCharacterContext);
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
            <Table.Tr h={TABLE_ROW_HEIGHT}>
                <Table.Th style={TABLE_STYLE}>{t('name')}</Table.Th>
                <Table.Td style={TABLE_STYLE}>{character?.description.name}</Table.Td>
            </Table.Tr>

            <Table.Tr h={TABLE_ROW_HEIGHT}>
                <Table.Th style={TABLE_STYLE}>{t('species')}</Table.Th>
                <Table.Td style={TABLE_STYLE}>
                    {character?.species.name + (character?.nation ? ' / ' + character.nation.name : '')}
                </Table.Td>
            </Table.Tr>

            <Table.Tr h={TABLE_ROW_HEIGHT}>
                <Table.Th style={TABLE_STYLE}>{t('profession')}</Table.Th>
                <Table.Td style={TABLE_STYLE}>{character?.description.profession}</Table.Td>
            </Table.Tr>
        </Table.Tbody>
    </Table>;
};

CharacterInfo.craft = {
    name: 'sheetEditor:characterInfo'
};