import {Table} from '@mantine/core';
import {TABLE_ROW_HEIGHT, TABLE_STYLE} from '../Constants';
import React, {useContext} from 'react';
import {useTranslation} from 'react-i18next';
import {PnPCharacterContext} from '../../../PnPCharacterContext';
import {TableNumberInput} from '../inputs/TableNumberInput';

/** Shows level and co of the character */
export function LevelInfo() {
    const {t} = useTranslation();
    const {characterForm, allowEdit} = useContext(PnPCharacterContext);

    return <Table
        variant="vertical"
        layout="fixed"
        withTableBorder
    >
        <Table.Tbody>
            <Table.Tr h={TABLE_ROW_HEIGHT}>
                <Table.Th style={TABLE_STYLE}>{t('character:level')}</Table.Th>
                <Table.Td style={TABLE_STYLE}>
                    <TableNumberInput
                        allowDecimal={false}
                        allowNegative={false}
                        readOnly={!allowEdit}
                        key={characterForm.key('level.level')}
                        {...characterForm.getInputProps('level.level')}
                    />
                </Table.Td>
            </Table.Tr>

            <Table.Tr h={TABLE_ROW_HEIGHT}>
                <Table.Th style={TABLE_STYLE}>{t('character:experiencePoints')}</Table.Th>
                <Table.Td style={TABLE_STYLE}>
                    <TableNumberInput
                        allowDecimal={false}
                        allowNegative={false}
                        readOnly={!allowEdit}
                        key={characterForm.key('level.experience')}
                        {...characterForm.getInputProps('level.experience')}
                    />
                </Table.Td>
            </Table.Tr>

            <Table.Tr h={TABLE_ROW_HEIGHT}>
                <Table.Th style={TABLE_STYLE}>{t('character:skillPoints')}</Table.Th>
                <Table.Td style={TABLE_STYLE}>
                    <TableNumberInput
                        allowDecimal={false}
                        allowNegative={false}
                        readOnly={!allowEdit}
                        key={characterForm.key('level.skillPoints')}
                        {...characterForm.getInputProps('level.skillPoints')}
                    />
                </Table.Td>
            </Table.Tr>
        </Table.Tbody>
    </Table>;
}