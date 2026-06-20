import {Table} from '@mantine/core';
import React, {useContext} from 'react';
import {TABLE_ROW_HEIGHT, TABLE_STYLE} from '../Constants';
import {useTranslation} from 'react-i18next';
import {PnPCharacterContext} from '../../../PnPCharacterContext';
import {CharacterDescription} from '../../../../../api/model';
import {TableTextarea} from '../inputs/TableTextarea';
import {PageElementSettings} from '../PageElementSettings';
import {ObjectSelect} from '../../../../input/ObjectSelect';

/** The description key with its human-readable name */
export type CharacterDescriptionSelectable = {
    id: keyof CharacterDescription;
    name: string;
}

/** Part to show text */
export function CharacterDescriptionInfo({
        description,
        setDescription
    }: {
        description: CharacterDescriptionSelectable;
        setDescription: (state: CharacterDescriptionSelectable) => void;
    }
) {
    const {t} = useTranslation();
    const {characterForm, allowEdit} = useContext(PnPCharacterContext);

    const descriptionOptions: CharacterDescriptionSelectable[] = [
        {id: 'appearance', name: t('character:appearance'),},
        {id: 'personality', name: t('character:personality'),},
        {id: 'goals', name: t('character:goals'),},
        {id: 'deficits', name: t('character:deficits'),},
        {id: 'affiliations', name: t('character:affiliations'),},
        {id: 'backstory', name: t('character:backstory')}
    ];

    return <>
        <Table
            withTableBorder
            striped
            style={{height: '100%', tableLayout: 'fixed'}}
        >
            <Table.Tbody>
                <Table.Tr h={TABLE_ROW_HEIGHT + 3}>
                    <Table.Th style={TABLE_STYLE}>{description?.name ?? '???'}</Table.Th>
                </Table.Tr>
                <Table.Tr>
                    <Table.Td
                        style={{whiteSpace: 'pre-line', textAlign: 'left', verticalAlign: 'top', ...TABLE_STYLE}}
                    >
                        {description !== null ?
                            <TableTextarea
                                readOnly={!allowEdit}
                                key={characterForm.key(`description.${description.id}`)}
                                {...characterForm.getInputProps(`description.${description.id}`)}
                            /> : null
                        }
                    </Table.Td>
                </Table.Tr>
            </Table.Tbody>
        </Table>
        <PageElementSettings>
            <ObjectSelect
                data={descriptionOptions}
                idKey="id"
                labelKey="name"
                value={description}
                onChange={d => setDescription(d)}
            />
        </PageElementSettings>
    </>;
}
