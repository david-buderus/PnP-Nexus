import {NumberInput, Table} from '@mantine/core';
import {TABLE_ROW_HEIGHT, TABLE_STYLE} from '../Constants';
import React, {useContext, useMemo} from 'react';
import {useTranslation} from 'react-i18next';
import {PnPCharacterContext} from '../../../PnPCharacterContext';
import {resourceFormatter, spellCastFormatter} from '../../../../utils/Formatters';
import {resizeArray} from '../../../../utils/Utils';
import {PageElementSettings} from '../PageElementSettings';


/** Shows the spells of the character */
export function SpellList({
    numberOfRows, setNumberOfRows
}: {
    numberOfRows: number;
    setNumberOfRows: (n: number) => void;
}) {
    const {t} = useTranslation();
    const {characterForm} = useContext(PnPCharacterContext);
    const character = characterForm.getValues();
    const spells = useMemo(() => resizeArray(character.spells, numberOfRows), [character.spells, numberOfRows]);

    return <>
        <Table
            withTableBorder
            withColumnBorders
            striped
            style={{tableLayout: 'fixed'}}
        >
            <Table.Tbody>
                <Table.Tr h={TABLE_ROW_HEIGHT}>
                    <Table.Th style={{width: '15%', ...TABLE_STYLE}}>{t('name')}</Table.Th>
                    <Table.Th style={{width: '45%', ...TABLE_STYLE}}>{t('effect')}</Table.Th>
                    <Table.Th style={{width: '8%', ...TABLE_STYLE}}>{t('spell:castTimeShort')}</Table.Th>
                    <Table.Th style={{width: '8%', ...TABLE_STYLE}}>{t('spell:cooldownShort')}</Table.Th>
                    <Table.Th style={{width: '12%', ...TABLE_STYLE}}>{t('spell:cost')}</Table.Th>
                    <Table.Th style={{width: '12%', ...TABLE_STYLE}}>{t('spell:cast')}</Table.Th>
                </Table.Tr>
                {spells.map((spell, index) => {
                    if (!spell) {
                        return <Table.Tr key={index} h={TABLE_ROW_HEIGHT}>
                            <Table.Td style={TABLE_STYLE}/>
                            <Table.Td style={TABLE_STYLE}/>
                            <Table.Td style={TABLE_STYLE}/>
                            <Table.Td style={TABLE_STYLE}/>
                            <Table.Td style={TABLE_STYLE}/>
                            <Table.Td style={TABLE_STYLE}/>
                        </Table.Tr>;
                    }
                    return <Table.Tr key={index} h={TABLE_ROW_HEIGHT}>
                        <Table.Td style={TABLE_STYLE}>{spell.name}</Table.Td>
                        <Table.Td style={TABLE_STYLE}>{spell.effect}</Table.Td>
                        <Table.Td style={TABLE_STYLE}>{spell.castTime}</Table.Td>
                        <Table.Td style={TABLE_STYLE}>{spell.cooldown}</Table.Td>
                        <Table.Td style={TABLE_STYLE}>{spell.cost?.map(resourceFormatter)?.join(', ') ?? ''}</Table.Td>
                        <Table.Td style={TABLE_STYLE}>{spellCastFormatter(spell.cast, t)}</Table.Td>
                    </Table.Tr>;
                })}
            </Table.Tbody>
        </Table>
        <PageElementSettings>
            <NumberInput
                label={t('sheetEditor:numberOfRows')}
                value={numberOfRows}
                onChange={e => setNumberOfRows(Number(e))}
                min={1}
                allowDecimal={false}
            />
        </PageElementSettings>
    </>;
}