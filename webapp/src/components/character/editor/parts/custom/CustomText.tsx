import {Stack, Table, TextInput, Tooltip} from '@mantine/core';
import React, {useContext} from 'react';
import {TABLE_ROW_HEIGHT, TABLE_STYLE} from '../Constants';
import {useTranslation} from 'react-i18next';
import {PnPCharacterContext} from '../../../PnPCharacterContext';
import {PageElementSettings} from '../PageElementSettings';

/** Part to show text from a custom field */
export function CustomText({
    title,
    customId,
    setTitle,
    setCustomId
}: {
    title: string;
    customId: string;
    setTitle: (s: string) => void;
    setCustomId: (s: string) => void;
}) {
    const {t} = useTranslation();
    const {characterForm} = useContext(PnPCharacterContext);
    const character = characterForm.getValues();

    return <>
        <Table
            withTableBorder
            striped
            style={{height: '100%', tableLayout: 'fixed'}}
        >
            <Table.Tbody>
                <Table.Tr h={TABLE_ROW_HEIGHT + 3}>
                    <Table.Th style={TABLE_STYLE}>{title}</Table.Th>
                </Table.Tr>
                <Table.Tr>
                    <Table.Td style={{whiteSpace: 'pre-line', ...TABLE_STYLE}}>
                        {character?.customFields?.[customId] ?? ''}
                    </Table.Td>
                </Table.Tr>
            </Table.Tbody>
        </Table>
        <PageElementSettings>
            <Stack>
                <TextInput
                    label={t('sheetEditor:title')}
                    value={title}
                    onChange={e => setTitle(e.currentTarget.value)}
                />
                <Tooltip label={t('sheetEditor:customFieldIdTooltip')}>
                    <TextInput
                        label={t('sheetEditor:customFieldId')}
                        value={customId}
                        onChange={e => setCustomId(e.currentTarget.value)}
                    />
                </Tooltip>
            </Stack>
        </PageElementSettings>
    </>;
}
