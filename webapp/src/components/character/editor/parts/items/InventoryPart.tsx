import {NumberInput, Stack, Table} from '@mantine/core';
import {TABLE_ROW_HEIGHT, TABLE_STYLE} from '../Constants';
import React, {useContext} from 'react';
import {useTranslation} from 'react-i18next';
import {PnPCharacterContext} from '../../../PnPCharacterContext';
import {PageElementSettings} from '../PageElementSettings';


/** Shows the inventory of the character */
export function InventoryPart({rows, columns, setRows, setColumns}: {
    rows: number;
    columns: number;
    setRows: (n: number) => void;
    setColumns: (n: number) => void;
}) {
    const {t} = useTranslation();
    const {characterForm} = useContext(PnPCharacterContext);
    const character = characterForm.getValues();

    return <>
        <Table
            withTableBorder
            withColumnBorders
            striped
            style={{tableLayout: 'fixed'}}
        >
            <Table.Tbody>
                <Table.Tr h={TABLE_ROW_HEIGHT}>
                    <Table.Th style={TABLE_STYLE} colSpan={columns}>{t('inventory')}</Table.Th>
                </Table.Tr>
                {Array.from({length: rows}, (_, rowIndex) =>
                    <Table.Tr h={TABLE_ROW_HEIGHT} key={rowIndex}>
                        {Array.from({length: columns}, (__, colIndex) => {
                            const itemStack = character?.inventory.inventory.items[rowIndex + colIndex * rows];

                            return <Table.Td style={{width: `${100 / columns}%`, ...TABLE_STYLE}} key={colIndex}>
                                {itemStack ? `${itemStack.stackSize}x ${itemStack.item.name}` : ''}
                            </Table.Td>;
                        })}
                    </Table.Tr>
                )}
            </Table.Tbody>
        </Table>
        <PageElementSettings>
            <Stack>
                <NumberInput
                    label={t('sheetEditor:numberOfRows')}
                    value={rows}
                    onChange={e => setRows(Number(e))}
                />
                <NumberInput
                    label={t('sheetEditor:numberOfColumns')}
                    value={columns}
                    onChange={e => setColumns(Number(e))}
                />
            </Stack>
        </PageElementSettings>
    </>;
}